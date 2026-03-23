package com.example.growtime.json_accessing;

public class Hardiness {
    private final int min;
    private final int max;

    public Hardiness(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
