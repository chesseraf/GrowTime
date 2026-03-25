package com.example.growtime;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUnitTest {
    @Test
    // for lychee
    public void t6ValidJsonObject1Lychee() throws Exception {
        String PLANT_JSON = "{"
                + "\"common_name\": \"lychee\","
                + "\"waterness\": \"average\","
                + "\"hardiness\": {"
                + "  \"min\": \"10\","
                + "  \"max\": \"11\""
                + "},"
                + "\"image_url\": \"https://cdn.britannica.com/18/176518-050-5AB1E61D/lychee-fruits-Southeast-Asia.jpg\""
                + "}";

        JSONObject jObject = new JSONObject(PLANT_JSON);
        JSONObject hard =  jObject.getJSONObject("hardiness");
        assertEquals("lychee", jObject.getString("common_name"));
        assertEquals("average", jObject.getString("waterness"));
        assertEquals("10", hard.getString("min"));
        assertEquals("11", hard.getString("max"));
        assertEquals("https://cdn.britannica.com/18/176518-050-5AB1E61D/lychee-fruits-Southeast-Asia.jpg", jObject.getString("image_url"));
    }

    @Test
    // for lychee
    public void t7ValidJsonObjectElephantGrass() throws Exception {
        String PLANT_JSON = "{"
                + "\"common_name\": \"elephant grass\","
                + "\"waterness\": \"average\","
                + "\"hardiness\": {"
                + "  \"min\": \"8\","
                + "  \"max\": \"11\""
                + "},"
                + "\"image_url\": \"https://www.feedipedia.org/sites/default/files/styles/standard__640x480_/public/images/elephant_grass_02.jpg?itok=_hG3AwOl\""
                + "}";

        JSONObject jObject = new JSONObject(PLANT_JSON);
        JSONObject hard =  jObject.getJSONObject("hardiness");
        assertEquals("elephant grass", jObject.getString("common_name"));
        assertEquals("average", jObject.getString("waterness"));
        assertEquals("8", hard.getString("min"));
        assertEquals("11", hard.getString("max"));
        assertEquals("https://www.feedipedia.org/sites/default/files/styles/standard__640x480_/public/images/elephant_grass_02.jpg?itok=_hG3AwOl", jObject.getString("image_url"));
    }

}
