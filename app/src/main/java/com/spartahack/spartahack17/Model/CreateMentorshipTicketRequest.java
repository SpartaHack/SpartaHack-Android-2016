package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 1/18/17
 * Spartahack-Android
 */
public class CreateMentorshipTicketRequest {
    private String channel;
    private String username;
    private String text;

    public CreateMentorshipTicketRequest(String channel, String username, String text) {
        this.channel = channel;
        this.username = username;
        this.text = text;
    }
}
