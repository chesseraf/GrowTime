package com.example.growtime.json_accessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckPlant {
    public List<Plant> AddPlant(List<Plant> pL, int hard_zone) {
        List<Plant> suitableP = new ArrayList<>();
        int count = 0;
        Collections.shuffle(pL);

        for (int i = 0; i < pL.size(); i++) {
            int min = pL.get(i).getH().getMin();
            int max = pL.get(i).getH().getMax();

            if (count == 5) {break;}
            if (hard_zone >= min && hard_zone <= max) {
                count++;
                setPlantContents(suitableP, pL, i);
            }
        }

        return suitableP;
    }

    public void setPlantContents(List<Plant> newL, List<Plant> pL, int c) {
        String name = pL.get(c).getCommon_name();
        String water = pL.get(c).getWaterness();
        String url = pL.get(c).getImage_url();
        Hardiness h = pL.get(c).getH();
        newL.add(new Plant(name, water, url, h));
    }
}
