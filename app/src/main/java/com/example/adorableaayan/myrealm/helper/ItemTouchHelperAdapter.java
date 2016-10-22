package com.example.adorableaayan.myrealm.helper;

/**
 * Created by AdorableAayan on 14-Oct-16.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);

}
