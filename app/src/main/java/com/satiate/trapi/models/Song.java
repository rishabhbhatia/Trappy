package com.satiate.trapi.models;

import lombok.Data;

/**
 * Created by Rishabh Bhatia on 24/11/16.
 */

public @Data class Song {

    private long id;
    private String title;
    private String artist;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }
}
