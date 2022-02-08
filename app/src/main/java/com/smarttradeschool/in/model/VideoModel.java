package com.smarttradeschool.in.model;

public class VideoModel {

    private String id;
    private String heading;
    private String message;
    private String video_url;
    private String added_date;

    public VideoModel(String id, String heading, String message, String video_url, String added_date) {
        this.id = id;
        this.heading = heading;
        this.message = message;
        this.video_url = video_url;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }
}
