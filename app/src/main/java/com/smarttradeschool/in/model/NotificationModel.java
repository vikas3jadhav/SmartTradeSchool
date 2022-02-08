package com.smarttradeschool.in.model;

public class NotificationModel {

    String heading;
    String added_date;

    public NotificationModel(String heading, String added_date, String type, String type_id) {
        this.heading = heading;
        this.added_date = added_date;
        this.type = type;
        this.type_id = type_id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    String type;
    String type_id;


}
