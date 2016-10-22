package com.example.adorableaayan.myrealm;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adorableaayan.myrealm.helper.ItemTouchHelperAdapter;
import com.example.adorableaayan.myrealm.helper.ItemTouchHelperViewHolder;
import com.example.adorableaayan.myrealm.helper.OnStartDragListener;
import com.example.adorableaayan.myrealm.model.User;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


/**
 * Created by AdorableAayan on 11-Oct-16.
 */
public class UserAdapter extends RealmRecyclerViewAdapter<User, UserAdapter.UserViewHolder> implements ItemTouchHelperAdapter
{
    OrderedRealmCollection<User>  usersList;
    Context mContext;
    private MainActivity activity;
    private final OnStartDragListener mDragStartListener;

    public UserAdapter(MainActivity activity, OrderedRealmCollection<User> usersList,  OnStartDragListener mDragStartListener){
        super(activity ,usersList, true);
        this.usersList = usersList;
        this.activity = activity;
        this.mDragStartListener = mDragStartListener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        CardView cardView_singleRow;
        TextView user_Name, user_PhoneNo;


        public UserViewHolder(View itemView) {
            super(itemView);
            cardView_singleRow = (CardView) itemView.findViewById(R.id.cardView_singleRow);
            user_Name = (TextView) itemView.findViewById(R.id.user_Name);
            user_PhoneNo = (TextView) itemView.findViewById(R.id.user_PhoneNo);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        UserViewHolder holder = new UserViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        User user = getData().get(position);
        holder.user_Name.setText(user.getName());
        holder.user_PhoneNo.setText(user.getPhoneNo());

        holder.cardView_singleRow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {

                    mDragStartListener.onStartDrag(holder);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
     return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        mDragStartListener.onMyItemDismiss(position);
    }


}
