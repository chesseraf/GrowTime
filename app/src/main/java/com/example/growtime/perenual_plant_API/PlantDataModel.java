package com.example.growtime.perenual_plant_API;

import java.io.Serializable;

public class PlantDataModel implements Serializable {
    private final int id;
    private final String common_name;
    private final Hardiness hardiness;

    private final String watering;

    public PlantDataModel(int id, String common_name, Hardiness hardiness, String watering) {
        this.id = id;
        this.common_name = common_name;
        this.hardiness = hardiness;
        this.watering = watering;
    }

    public int getKey() {
        return id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public Hardiness getHardiness() {
        return hardiness;
    }

    public String getWatering() {
        return watering;
    }
}
