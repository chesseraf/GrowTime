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
public class InstrumentedTestII {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void test16_plantSearchChinaRoseSuitableInHonExt() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();

        onView(withId(R.id.h_zipcode_input))
                .perform(replaceText("01844"), closeSoftKeyboard());
        onView(withId(R.id.plant_name))
                .perform(replaceText("china rose"), closeSoftKeyboard());
        onView(withId(R.id.sub_hon))
                .perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.hon_result)).check(matches(withText("china rose can be suitably grown here\n")));
    }

    @Test
    public void test17_plantSearchSilverWillowSuitableInHonExt() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();

        onView(withId(R.id.h_zipcode_input))
                .perform(replaceText("01844"), closeSoftKeyboard());
        onView(withId(R.id.plant_name))
                .perform(replaceText("Silver Willow"), closeSoftKeyboard());
        onView(withId(R.id.sub_hon))
                .perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.hon_result)).check(matches(withText("silver willow can be suitably grown here\n")));
    }

    @Test
    public void test18_plantSearchMagnosteenTooColdInHonExt() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();

        onView(withId(R.id.h_zipcode_input))
                .perform(replaceText("01844"), closeSoftKeyboard());
        onView(withId(R.id.plant_name))
                .perform(replaceText("mangosteen"), closeSoftKeyboard());
        onView(withId(R.id.sub_hon))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.hon_result)).check(matches(withText("It's too cold for mangosteen to be grown here\n")));
    }

    @Test
    public void test19_plantSearchBloodBananaTooColdInHonExt() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();

        onView(withId(R.id.h_zipcode_input))
                .perform(replaceText("01844"), closeSoftKeyboard());
        onView(withId(R.id.plant_name))
                .perform(replaceText("blood banana"), closeSoftKeyboard());
        onView(withId(R.id.sub_hon))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.hon_result)).check(matches(withText("It's too cold for blood banana to be grown here\n")));
    }

    @Test
    public void test20_plantSearchAlpineFirTooHotInHonExt() {
        init();
        onView(withId(R.id.Honors_button)).perform(click());
        intended(hasComponent(HonExtSceneActivity.class.getName()));
        release();

        onView(withId(R.id.h_zipcode_input))
                .perform(replaceText("01844"), closeSoftKeyboard());
        onView(withId(R.id.plant_name))
                .perform(replaceText("Alpine Fir"), closeSoftKeyboard());
        onView(withId(R.id.sub_hon))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.hon_result)).check(matches(withText("It's too hot for alpine fir to be grown here\n")));
    }
}
