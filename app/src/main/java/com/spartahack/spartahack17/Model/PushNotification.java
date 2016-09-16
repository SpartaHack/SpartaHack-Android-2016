package com.spartahack.spartahack17.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryancasler on 12/22/15.
 */
public class PushNotification extends RealmObject {

    /** Full message of the push */
    private String message;

    /** Title of the push */
    private String title;

    /** Type of the push (announce, silent, update) */
    private int type;

    /** Push id */
    @PrimaryKey
    private int id;

    /** if the push should be pinned or not */
    private int pinned;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {this.type = type;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getPinned() {return pinned;}

    public void setPinned(int pinned) {this.pinned = pinned;}

}
