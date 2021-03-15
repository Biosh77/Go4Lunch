package com.example.go4lunch;


import android.database.Observable;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.go4lunch.googlemapsretrofit.RetrofitMaps;
import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.AutoComplete;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.NearbySearch;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.meta.When;

import io.reactivex.observers.TestObserver;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.close;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
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


    public static class GoogleAPITest {


        @Test
        public void myTest() throws InterruptedException {
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
        public void myRestaurantList_shouldNotBeEmpty() throws InterruptedException {
            Thread.sleep(10000);
            onView(withId(R.id.List_View)).perform(click());
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
}