package com.andromeda.ara.util;

public class FullModel {
    public String title;
    public String link;
    public String description;
    public String description2;
    public String image;
    String[] imageList;

    public FullModel(String title, String link, String description, String image, String description2, String[] imageList) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.description2 = description2;
        this.image = image;
        this.imageList = imageList;
    }
}
