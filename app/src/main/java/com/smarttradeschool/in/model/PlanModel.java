package com.smarttradeschool.in.model;

public class PlanModel {

    private String id;
    private String name;
    private String price;
    private String days;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public PlanModel(String id, String name, String price, String days) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.days = days;
    }
}
