package com.example.growtime;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.Intents.intended;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

// for entering a text
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

// for ordering
import org.junit.FixMethodOrder;        // type this manually
import org.junit.runners.MethodSorters; // type this manually

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.growtime.json_accessing.Hardiness;
import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.Plant;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTestJava {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void test1_recommendButton_opensRecommendScene() {
        init();
        onView(withId(R.id.RecommendButton)).perform(click());
        intended(hasComponent(RecommendSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test2_typeInZipCode_shouldWork() {

        onView(withId(R.id.zipcode_input))
                .perform(replaceText("01852"), closeSoftKeyboard());

        onView(withId(R.id.sub_butt))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.zip_res))
                .check(matches(withText("Zip: 01852")));
    }

    @Test
    public void test3_honorsButton_opensHonExtScene() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test4_user_should_type_in_plant() {
        onView(withId(R.id.plant_name))
                .perform(replaceText("tulips"));

        onView(withId(R.id.plant_name))
                .check(matches(withText("01852")));
    }

    @Test
    public void test5_locationButton_opensLocationSceneActivity() {
        init();
        onView(withId(R.id.LocationButton)).perform(click());
        intended(hasComponent(LocationSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test8_MyGardenButton_opensMyPlantsScene() {
        init();
        onView(withId(R.id.MyGarden)).perform(click());
        intended(hasComponent(MyPlantsSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test9_addPlantsButton_AddPlantsScene() {
        init();
        onView(withId(R.id.addPlantHome)).perform(click());
        intended(hasComponent(AddPlantSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test10_editButton_opensEditPlantScene() {
        init();
        onView(withId(R.id.editPlantHome)).perform(click());
        intended(hasComponent(EditPlantSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test11_navRec_opensRecommendScene() {
        init();
        onView(withId(R.id.nav_recommend)).perform(click());
        intended(hasComponent(RecommendSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test12_navHonors_opensHonExitScene() {
        init();
        onView(withId(R.id.nav_honors)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();
    }

    @Test
    public void test13_navMyPlants_openMyPlantsScene() {
        init();
        onView(withId(R.id.nav_my_plants)).perform(click());
        intended(hasComponent(MyPlantsSceneActivity.class.getName()));
        release();
    }
    @Test
    public void test14_MyGardenStartsEmpty() {
        init();
        onView(withId(R.id.MyGarden)).perform(click());
        intended(hasComponent(MyPlantsSceneActivity.class.getName()));
        release();

        // Check MyGarden has no items
        onView(withId(R.id.my_garden_recycler))
                .check(matches(hasChildCount(0)));

        // Check empty message is displayed
        onView(withId(R.id.my_garden_empty))
                .check(matches(isDisplayed()));
    }
    @Test
    public void test15_recPlantAddedToMyGarden() {
        init();
        onView(withId(R.id.RecommendButton)).perform(click());
        intended(hasComponent(RecommendSceneActivity.class.getName()));
        release();

        // get plant recommendations with zip code
        onView(withId(R.id.zipcode_input))
                .perform(replaceText("60601"), closeSoftKeyboard());
        onView(withId(R.id.sub_butt))
                .perform(click());

        // Wait for recommendation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add to Garden
        onView(allOf(
                withId(R.id.btn_add_to_garden),
                isDisplayed()
        )).perform(click());


        // Go to MyGarden
        init();
        onView(withId(R.id.nav_my_plants)).perform(click());
        intended(hasComponent(MyPlantsSceneActivity.class.getName()));
        release();

        // Wait for MyGarden to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check the plant added is there
        onView(allOf(
                withId(R.id.plant_name),
                isDisplayed()
        )).check(matches(isDisplayed()));
    }


}