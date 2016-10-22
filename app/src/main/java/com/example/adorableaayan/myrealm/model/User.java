package com.example.adorableaayan.myrealm.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by AdorableAayan on 11-Oct-16.
 */
public class User extends RealmObject{

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    private String          name;
    private String          phoneNo; @Ignore
    private int             sessionId;


    public int    getSessionId() { return sessionId; }
    public void   setSessionId(int sessionId) { this.sessionId = sessionId; }
}
