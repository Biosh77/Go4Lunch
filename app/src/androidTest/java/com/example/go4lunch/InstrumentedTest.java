package com.example.go4lunch;


import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.close;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class InstrumentedTest {

    private List<String> workmatesList = new ArrayList<>();

    @Rule
    public ActivityTestRule<AccueilActivity> AccueilActivityRule = new ActivityTestRule<>(AccueilActivity.class);

    @Before
    public void initialize(){
    }

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
    public void testNavigationMenuItemClick() {
        onView(withId(R.id.accueil_drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.accueil_drawer_layout))
                .check(matches(isOpen()));
        onView(withId(R.id.accueil_drawer_layout))
                .perform(navigateTo(R.id.accueil_drawer_settings));

        onView(isRoot()).perform(pressBack());

        onView(withId(R.id.accueil_drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.accueil_drawer_layout))
                .check(matches(isOpen()));
        onView(withId(R.id.accueil_drawer_layout))
                .perform(navigateTo(R.id.accueil_drawer_logout));
    }

    @Test
    public void testSearchButtonBar() {
        onView(withId(R.id.accueil_toolbar)).perform(navigateTo(R.id.search_restaurant));
        onView(withId(R.id.search_restaurant)).perform(click());
        onView(withId(R.id.search_restaurant)).perform(typeText("bar"));
    }

    @Test
    public void testDetailClick(){
        onView(withId(R.id.List_View)).perform(click())
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.list_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

}