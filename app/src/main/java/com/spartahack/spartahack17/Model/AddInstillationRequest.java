package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 1/18/17
 * Spartahack-Android
 */
public class AddInstillationRequest {
    public String device_type;
    public String token;

    public AddInstillationRequest(String token) {
        this.device_type = "android";
        this.token = token;
    }
}