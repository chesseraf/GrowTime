package com.example.growtime;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.Intents.intended;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTestJava {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void recommendButton_opensRecommendScene() {

        init();

        onView(withId(R.id.RecommendButton)).perform(click());

        intended(hasComponent(RecommendSceneActivity.class.getName()));

        release();
    }

    @Test
    public void honorsButton_opensHonExtScene() {

        init();

        onView(withId(R.id.Honors_button)).perform(click());

        intended(hasComponent(HonExtSceneActivity.class.getName()));

        release();
    }
}