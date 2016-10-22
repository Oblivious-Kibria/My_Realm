package com.example.adorableaayan.myrealm;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.adorableaayan.myrealm.helper.OnStartDragListener;
import com.example.adorableaayan.myrealm.helper.SimpleItemTouchHelperCallback;
import com.example.adorableaayan.myrealm.model.User;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnStartDragListener {

    RecyclerView userList_RecyclerView;
    FloatingActionButton fab;
    private Realm realm;
    public MyTestReceiver receiverForTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        receiverForTest = new MyTestReceiver(new Handler());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userList_RecyclerView = (RecyclerView) findViewById(R.id.userList_RecyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddUserDialog();
            }
        });

        realm = Realm.getDefaultInstance();

        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showAddUserDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.user_input, null);
        dialogBuilder.setView(dialogView);

        final EditText userName = (EditText) dialogView.findViewById(R.id.userName);
        final EditText userPhoneNo = (EditText) dialogView.findViewById(R.id.userPhoneNo);

        dialogBuilder.setTitle("Add New User");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                basicCRUD(realm, userName.getText().toString(), userPhoneNo.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    private void basicCRUD(Realm realm, final String userName, final String userPhoneNo) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class);
                user.setName(userName);
                user.setPhoneNo(userPhoneNo);
            }
        });

    }

    private ItemTouchHelper mItemTouchHelper;

    private void setUpRecyclerView() {
        userList_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        UserAdapter userAdapter = new UserAdapter(MainActivity.this, realm.where(User.class).findAllAsync(), this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(userAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(userList_RecyclerView);
        userList_RecyclerView.setAdapter(userAdapter);
        userList_RecyclerView.setHasFixedSize(true);

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }


    @Override
    public void onMyItemDismiss(final int position) {

//        Toast.makeText(MainActivity.this,""+realm.where(User.class).findAllAsync().get(position).getName(), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, PollingService.class);
        i.putExtra("receiver", receiverForTest);
        i.putExtra("user_name", realm.where(User.class).findAllAsync().get(position).getName());
        startService(i);

        setupServiceReceiver();
    }

    private void setupServiceReceiver() {

        // This is where we specify what happens when data is received from the service
        receiverForTest.setReceiver(new MyTestReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String user_name = resultData.getString("user_name");
                    Toast.makeText(MainActivity.this, user_name+ " Successfully Deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
