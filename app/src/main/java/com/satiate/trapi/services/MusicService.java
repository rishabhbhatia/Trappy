package com.satiate.trapi.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

import com.satiate.trapi.models.Song;

import co.mobiwise.library.InteractivePlayerView;

/**
 * Created by Rishabh Bhatia on 24/11/16.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosn;
    private InteractivePlayerView ipv;

    private final IBinder musicBind = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        songPosn=0;
    }

    public void initMusicPlayer(MediaPlayer mediaPlayer, InteractivePlayerView ipv)           //set player properties
    {

        this.player = mediaPlayer;
        this.ipv = ipv;

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public class MusicBinder extends Binder {
       public MusicService getService() {
            return MusicService.this;
        }
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public void playSong()  //play a song
    {
        player.reset();
        ipv.stop();

        Song playSong = songs.get(songPosn);
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("rishabh", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        if(mp.getDuration() != 0)
        {
            ipv.setMax(mp.getDuration()/1000); // music duration in seconds.
            Log.d("rishabh", "hello music dur "+mp.getDuration()/1000);
        }else
        {
            ipv.setMax(1);
        }

        ipv.start();
    }
}
