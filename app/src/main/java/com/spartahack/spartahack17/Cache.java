package com.spartahack.spartahack17;

/**
 * Created by ryan on 11/5/15.
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

    /**
     * enum for user roles
     */
    public enum ROLE {ADMIN, ATTENDEE, JUDGE}

    /**
     * The role that the current user has
     */
    private ROLE role;

    /**
     * URL of the users qr code
     */
    private String qrURL;

    /**
     * Username of the user
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ROLE getRole() {
        return role;
    }

    public String getQrURL() {
        return qrURL;
    }

    public void setRole(String roleString){
        switch (roleString) {
            case "admin":
                role = ROLE.ADMIN;
                break;
            case "judge":
                role = ROLE.JUDGE;
                break;
            default:
                role = ROLE.ATTENDEE;
                break;
        }
    }

    public void setQrURL(String url){qrURL = url;}

}
