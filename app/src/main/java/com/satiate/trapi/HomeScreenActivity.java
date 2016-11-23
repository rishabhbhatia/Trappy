package com.satiate.trapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;

public class HomeScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 112;
    private AudioVisualization audioVisualization;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        audioVisualization = (AudioVisualization) findViewById(R.id.visualizer_view);

        mediaPlayer = MediaPlayer.create(HomeScreenActivity.this, R.raw.sample);
        mediaPlayer.start();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED)
        {
            setupMediaVisualization(mediaPlayer);
        }else
        {
            requestPermissions();
        }

    }

    private void setupMediaVisualization(MediaPlayer mediaPlayer)
    {
        VisualizerDbmHandler handler = DbmHandler.Factory.newVisualizerHandler(mediaPlayer);
        audioVisualization.linkTo(handler);
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS},
                REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioVisualization.onResume();
    }

    @Override
    protected void onPause() {
        audioVisualization.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        audioVisualization.release();
        if(mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }

            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean bothGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.RECORD_AUDIO.equals(permissions[i]) || Manifest.permission.MODIFY_AUDIO_SETTINGS.equals(permissions[i])) {
                    bothGranted &= grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
            if (bothGranted) {
                setupMediaVisualization(mediaPlayer);
            } else {
                requestPermissions();
            }
        }
    }
}
