package com.m68476521.mike.baking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.m68476521.mike.baking.activities.BakingActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Test to verify is the grid view is visible
 */

@RunWith(AndroidJUnit4.class)

public class IntentActivityBasicTest {

    private static final String TEST_NUTELLA_PIE_NAME = "Nutella Pie";

    @Rule
    public final ActivityTestRule<BakingActivity> mBakingActivityIntentsTestRule = new
            ActivityTestRule<>(BakingActivity.class);

    @Before
    public void init() {
        mBakingActivityIntentsTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void clickGridViewItem_OpensRecipeActivity() {
        onView(withId(R.id.recipes_grid)).check(matches(isDisplayed()));
    }

    @Test
    public void clickGridViewItem_ShowNutellaPieTitle() {
        onView(withId(R.id.recipes_grid)).perform(click());
        onView(allOf(withId(R.id.title_recipe), withText(TEST_NUTELLA_PIE_NAME)));
    }
}
