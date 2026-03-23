package com.example.growtime.json_accessing;

public class Plant {
    private final String common_name;
    private final String waterness;
    private final String image_url;
    private final Hardiness h;

    public Plant(String common_name, String waterness, String image_url, Hardiness h) {
        this.common_name = common_name;
        this.waterness = waterness;
        this.image_url = image_url;
        this.h = h;
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getWaterness() {
        return waterness;
    }

    public String getImage_url() {
        return image_url;
    }

    public Hardiness getH() {
        return h;
    }
}
