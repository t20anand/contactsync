package com.anand.contactsync;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3572976);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3596768);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction constraintLayout = onView(
                withId(R.id.tempImageCont));
        constraintLayout.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Choose from Gallery"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3592761);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editTextPersonName));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.editTextPersonName));
        appCompatEditText2.perform(scrollTo(), replaceText("Anand"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.editTextMobileNo));
        appCompatEditText3.perform(scrollTo(), replaceText("9845231811"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.editTextPhoneNo));
        appCompatEditText4.perform(scrollTo(), replaceText("25867"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                withId(R.id.editTextEmail));
        appCompatEditText5.perform(scrollTo(), replaceText("anand@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                withId(R.id.editTextAddress));
        appCompatEditText6.perform(scrollTo(), replaceText("Kathmandu"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_save), withContentDescription("Save"), isDisplayed()));
        actionMenuItemView.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
