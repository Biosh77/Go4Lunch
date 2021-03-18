package com.example.go4lunch;


import android.widget.AutoCompleteTextView;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.close;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class InstrumentedTest {


    @Rule
    public ActivityScenarioRule<AccueilActivity> rule = new ActivityScenarioRule<>(AccueilActivity.class);


        @Test
        public void testBottomNavigationViewItemClick() {
            onView(withId(R.id.Map_View_main)).perform(click())
                    .check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.List_View)).perform(click())
                    .check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.Workmates)).perform(click())
                    .check(matches(isCompletelyDisplayed()));
        }

        @Test
        public void testNavigationDrawerOpenAndClose() {
            onView(withId(R.id.accueil_drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.accueil_drawer_layout))
                    .check(matches(isOpen()))
                    .perform(close())
                    .check(matches(not(isOpen())));
        }


        @Test
        public void googleMapClick() throws InterruptedException {
            Thread.sleep(10000);
            onView(withContentDescription("Google Map")).perform(click());
        }

        @Test
        public void testSearchButtonBar() throws InterruptedException {
            Thread.sleep(20000);
            onView(withId(R.id.search_restaurant)).perform(click());
            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("bar"));
        }

        @Test
        public void testDetailClick() throws InterruptedException {
            onView(withId(R.id.List_View)).perform(click())
                    .check(matches(isCompletelyDisplayed()));
            Thread.sleep(10000);
            onView(withId(R.id.list_recycler))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }


        @Test
        public void testNavigationMenuItemClick() throws InterruptedException {
            Thread.sleep(10000);
            onView(withId(R.id.accueil_drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.accueil_drawer_layout))
                    .check(matches(isOpen()));
            onView(withId(R.id.accueil_drawer_yourlunch))
                    .perform(click());

            onView(isRoot()).perform(pressBack());

            onView(withId(R.id.accueil_drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.accueil_drawer_layout))
                    .check(matches(isOpen()));
            onView(withId(R.id.accueil_drawer_settings))
                    .perform(click());

            onView(isRoot()).perform(pressBack());

            onView(withId(R.id.accueil_drawer_layout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.accueil_drawer_layout))
                    .check(matches(isOpen()));
            onView(withId(R.id.accueil_drawer_logout))
                    .perform(click());
        }




}