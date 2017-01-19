package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 1/18/17
 * Spartahack-Android
 */
@SuppressWarnings("FieldCanBeLocal")
public class AddInstillationRequest {
    private final String device_type;
    private final String token;

    public AddInstillationRequest(String token) {
        this.device_type = "android";
        this.token = token;
    }
}