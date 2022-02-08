package com.smarttradeschool.in.model;

public class MessageModel {

    private String id;
    private String heading;
    private String description;
    private String added_date;

    public MessageModel(String id, String heading, String description, String added_date) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.added_date = added_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }
}
