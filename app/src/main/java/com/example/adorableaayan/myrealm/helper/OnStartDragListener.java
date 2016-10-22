package com.example.adorableaayan.myrealm.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by AdorableAayan on 14-Oct-16.
 */
public interface OnStartDragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void onMyItemDismiss(int position);
}
