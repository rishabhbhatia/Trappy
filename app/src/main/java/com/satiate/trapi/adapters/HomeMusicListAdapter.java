package com.satiate.trapi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satiate.trapi.R;
import com.satiate.trapi.models.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.claucookie.miniequalizerlibrary.EqualizerView;

/**
 * Created by Rishabh Bhatia on 24/11/16.
 */

public class HomeMusicListAdapter extends RecyclerView.Adapter<HomeMusicListAdapter.HomeMusicListViewHolder> {


    private Context context;
    private ArrayList<Song> songs;

    public HomeMusicListAdapter(Context context, ArrayList<Song> songs) {
        try {
            this.context = context;
            this.songs = songs;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public HomeMusicListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        try {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.row_home_music_list, parent, false);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new HomeMusicListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeMusicListViewHolder holder, final int position) {

        holder.clear();

        Song song = songs.get(position);

        holder.equalizerView.animateBars();

        holder.tvSongName.setText(song.getTitle());
        holder.tvSongArtist.setText(song.getArtist());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class HomeMusicListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.equalizer_view_song_row)
        EqualizerView equalizerView;
        @BindView(R.id.tv_name_song_row)
        TextView tvSongName;
        @BindView(R.id.tv_artist_song_row)
        TextView tvSongArtist;
        @BindView(R.id.iv_icon_song_row)
        ImageView ivSongThumb;

        public HomeMusicListViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void clear() {
            tvSongName.setText("");
            tvSongArtist.setText("");
            ivSongThumb.setImageBitmap(null);
        }
    }

}

