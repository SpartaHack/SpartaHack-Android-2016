package com.spartahack.spartahack17;

import com.spartahack.spartahack17.Model.Session;

/**
 * Created by ryan on 11/5/15
 * SpartaHack2016-Android
 */
public class Cache {

    /**
     * Singleton instance of the cache
     */
    public static final Cache INSTANCE = new Cache();

    /**
     * Constructor is private for singleton pattern
     */
    private Cache() {}

    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void clear(){
        session = null;
    }

    public boolean hasSession() {
        return session != null;
    }
}
