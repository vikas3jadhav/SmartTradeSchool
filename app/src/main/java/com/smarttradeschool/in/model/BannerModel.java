package com.smarttradeschool.in.model;

public class BannerModel {

    private String id;

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    private String heading;
    private String description;
    private String image_url;
    private String added_date;

    public BannerModel(String id, String heading, String description, String image_url, String added_date) {
        this.id = id;
        this.heading = heading;
        this.description = description;
        this.image_url = image_url;
        this.added_date = added_date;
    }
}
