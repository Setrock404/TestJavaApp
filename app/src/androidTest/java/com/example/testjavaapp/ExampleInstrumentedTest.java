package com.example.testjavaapp;

import  androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<UnitTestActivity> mActivityRule =
            new ActivityTestRule<>(UnitTestActivity.class);

    @Test
    public void checkIfResizeWorksFine() {
        // Type text and then press the button.
        onView(withId(R.id.et_view_size))
                .perform(typeText("150"));
        closeSoftKeyboard();

        onView(withId(R.id.et_bitmap_size))
                .perform(typeText("200"));
        closeSoftKeyboard();

        onView(withId(R.id.btn_calc_size)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.tv_res_size)).check(matches(withText("Result size is:150")));
    }

    @Test
    public void checkIfInputIsValid() {
        // Type text and then press the button.
        onView(withId(R.id.et_view_size))
                .perform(typeText("0"));
        closeSoftKeyboard();

        onView(withId(R.id.et_bitmap_size))
                .perform(typeText("200"));
        closeSoftKeyboard();

        onView(withId(R.id.btn_calc_size)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.tv_res_size)).check(matches(withText(R.string.wrong_input)));
    }
}
