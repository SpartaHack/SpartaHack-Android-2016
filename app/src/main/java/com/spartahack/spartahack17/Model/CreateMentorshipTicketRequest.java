package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 1/18/17
 * Spartahack-Android
 */
@SuppressWarnings("FieldCanBeLocal")
public class CreateMentorshipTicketRequest {
    private final String channel;
    private final String username;
    private final String text;

    public CreateMentorshipTicketRequest(String channel, String username, String text) {
        this.channel = channel;
        this.username = username;
        this.text = text;
    }
}
