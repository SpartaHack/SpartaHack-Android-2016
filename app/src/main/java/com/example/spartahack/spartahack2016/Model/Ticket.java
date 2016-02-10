package com.example.spartahack.spartahack2016.Model;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ticket extends RealmObject implements Serializable {

    // Subject of the Ticket
    private String subject;

    // Category the ticket was submitted under
    private String category;

    private String subcategory;

    // Description of the ticket
    private String description;

    private String location;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name){this.name = name;}

    private boolean mine = false;

    public boolean getMine() {
        return mine;
    }

    public void setMine(boolean mine){
        this.mine = mine;
    }

    @PrimaryKey
    private String id;

    public void setId(String id){this.id = id;}

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String s){status = s;}

    private String status;

    public Ticket() {}

    public Ticket(String subject, String description, String status, String id, String subcategory, String location, String name) {
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.id = id;
        this.subcategory = subcategory;
        this.location = location;
        this.name = name;
    }
        public Ticket(String subject, String description, String status, String id, String subcategory, String location) {
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.id = id;
        this.subcategory = subcategory;
        this.location = location;
    }

    public Ticket(String subject, String description, String status, String id, String subcategory, String location, boolean mine) {
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.id = id;
        this.subcategory = subcategory;
        this.location = location;
        this.mine = mine;
    }

    public Ticket(String subject, String description, String status, String id, String subcategory, String location, boolean mine, String name) {
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.id = id;
        this.subcategory = subcategory;
        this.location = location;
        this.mine = mine;
        this.name = name;
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


    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
