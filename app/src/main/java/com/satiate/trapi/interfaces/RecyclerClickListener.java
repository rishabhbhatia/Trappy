package com.satiate.trapi.interfaces;

import android.view.View;

/**
 * Created by Rishabh Bhatia on 11/25/2016.
 */
public interface RecyclerClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
