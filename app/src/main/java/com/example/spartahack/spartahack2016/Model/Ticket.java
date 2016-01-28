package com.example.spartahack.spartahack2016.Model;


import java.io.Serializable;

public class Ticket implements Serializable {

    // Subject of the Ticket
    private String subject;

    // Category the ticket was submitted under
    private String category;

    // Description of the ticket
    private String description;

    public String getStatus() {
        return status;
    }

    private String status;

    public Ticket(String subject, String category, String description, String status) {
        this.subject = subject;
        this.category = category;
        this.description = description;
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
