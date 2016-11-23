package com.satiate.trapi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.satiate.trapi.R;

import es.claucookie.miniequalizerlibrary.EqualizerView;

/**
 * Created by Rishabh Bhatia on 24/11/16.
 */

public class HomeMusicListAdapter extends RecyclerView.Adapter<HomeMusicListAdapter.HomeMusicListViewHolder> {


    private Context context;

    public HomeMusicListAdapter(Context context) {
        try {
            this.context = context;
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

        holder.equalizerView.animateBars();
    }

    @Override
    public int getItemCount() {
        return 30;
    }   //TODO

    static class HomeMusicListViewHolder extends RecyclerView.ViewHolder {

        private EqualizerView equalizerView;

        public HomeMusicListViewHolder(View itemView) {
            super(itemView);
            try {
                equalizerView = (EqualizerView) itemView.findViewById(R.id.equalizer_view);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void clear() {

        }
    }

}

