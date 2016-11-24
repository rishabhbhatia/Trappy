package com.satiate.trapi;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.satiate.trapi.adapters.HomeMusicListAdapter;
import com.satiate.trapi.models.Song;
import com.satiate.trapi.services.MusicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import co.mobiwise.library.InteractivePlayerView;
import co.mobiwise.library.OnActionClickedListener;

public class HomeScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 112;
    private AudioVisualization audioVisualization;
    private MediaPlayer mediaPlayer;
    private InteractivePlayerView ipv;
    private RecyclerView rvMusic;
    private ArrayList<Song> songs;

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        audioVisualization = (AudioVisualization) findViewById(R.id.visualizer_view);
        ipv = (InteractivePlayerView) findViewById(R.id.ipv);
        rvMusic = (RecyclerView) findViewById(R.id.rv_music_home_screen);

        mediaPlayer = MediaPlayer.create(HomeScreenActivity.this, R.raw.sample);
        mediaPlayer.start();

        ipv.setMax(mediaPlayer.getDuration()/1000); // music duration in seconds.
        Log.d("rishabh", "hello music dur "+mediaPlayer.getDuration()/1000);

        songs = new ArrayList<>();

        ipv.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(int id) {
                switch (id){
                    case 1:
                        //Called when 1. action is clicked.
                        break;
                    case 2:
                        //Called when 2. action is clicked.
                        break;
                    case 3:
                        //Called when 3. action is clicked.
                        break;
                    default:
                        break;
                }
            }
        });

        ipv.start();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED)
        {
            setupMediaVisualization(mediaPlayer);
        }else
        {
            requestPermissions();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void setupMusicList()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeScreenActivity.this);
        rvMusic.setLayoutManager(linearLayoutManager);
        rvMusic.setAdapter(new HomeMusicListAdapter(HomeScreenActivity.this, songs));
    }

    private void setupMediaVisualization(MediaPlayer mediaPlayer)
    {
        VisualizerDbmHandler handler = DbmHandler.Factory.newVisualizerHandler(mediaPlayer);
        audioVisualization.linkTo(handler);

        getSongList();

        Collections.sort(songs, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        setupMusicList();
    }

    public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS},
                REQUEST_CODE);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

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

    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songs.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;

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

}
