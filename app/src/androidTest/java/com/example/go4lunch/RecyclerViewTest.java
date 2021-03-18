package com.example.go4lunch;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4ClassRunner.class)
public class RecyclerViewTest {


    @Rule
    public ActivityScenarioRule<AccueilActivity> rule = new ActivityScenarioRule<>(AccueilActivity.class);

    @Test
    public void myRestaurantList_shouldNotBeEmpty() throws InterruptedException {
        onView(withId(R.id.List_View)).perform(click());
        Thread.sleep(10000);
        onView(ViewMatchers.withId(R.id.List_View))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void myWorkmatesList_shouldNotBeEmpty() throws InterruptedException {
        onView(withId(R.id.Workmates)).perform(click());
        Thread.sleep(20000);
        onView(ViewMatchers.withId(R.id.workmates_recycler))
                .check(matches(hasMinimumChildCount(1)));
    }

}
