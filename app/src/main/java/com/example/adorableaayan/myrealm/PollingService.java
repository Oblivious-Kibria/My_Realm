package com.example.adorableaayan.myrealm;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.adorableaayan.myrealm.model.User;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by AdorableAayan on 15-Oct-16.
 */
public class PollingService extends IntentService {
    public PollingService() {
        super("PollingService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        Log.d("Service", "Service");
        final ResultReceiver rec = intent.getParcelableExtra("receiver");
        final String user_name = intent.getStringExtra("user_name");

        if (intent.getExtras() != null) {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // To send a message to the Activity, create a pass a Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", user_name);
                    // Here we call send passing a resultCode and the bundle of extras
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            RealmResults<User> user = bgRealm.where(User.class).equalTo("name", user_name).findAll();
                            user.deleteAllFromRealm();
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // Original queries and Realm objects are automatically updated.
                        }
                    });

                    rec.send(Activity.RESULT_OK, bundle);
                }
            });
        }
    }
}

