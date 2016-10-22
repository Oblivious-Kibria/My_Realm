package com.example.adorableaayan.myrealm;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by AdorableAayan on 11-Oct-16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
