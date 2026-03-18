package com.example.growtime.access_hardiness_zone;
import java.io.Serializable;
import java.util.ArrayList;
public class DataModel implements Serializable {
    private String zone;
    private String temperature_zone;
    private Coordinates c;

    public DataModel(String zone, String temperature_zone, Coordinates c) {
        this.zone = zone;
        this.temperature_zone = temperature_zone;
        this.c = c;
    }

    // Returns the zone string (e.g., "6b")
    public String getZone() {
        return zone;
    }

    // Returns the temperature range description
    public String getTemperatureZone() {
        return temperature_zone;
    }

    // Returns the nested Coordinates object
    public Coordinates getCoordinates() {
        return c;
    }
}
