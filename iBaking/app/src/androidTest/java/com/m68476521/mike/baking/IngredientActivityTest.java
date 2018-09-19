package com.m68476521.mike.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.m68476521.mike.baking.activities.IngredientActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test to verify intents extra values
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IngredientActivityTest {
    private static final String TEST_SALT_NAME = "salt";
    private static final String TEST_MEASURE_NAME = "TSP";
    private static final String TEST_QUANTITY_NAME = "1.5";

    @Rule
    public final ActivityTestRule<IngredientActivity> mainActivityActivityTestRule =
            new ActivityTestRule<IngredientActivity>(IngredientActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, IngredientActivity.class);
                    intent.putExtra("recipe_id", "1");
                    return intent;
                }
            };

    @Before
    public void init() {
        mainActivityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void demonstrateIntentPrep() {
        onView(withId(R.id.title_recipe)).check(matches(isDisplayed()));
    }

    @Test
    public void demonstrateIngredient() {
        onView(Matchers.allOf(withId(R.id.title_recipe), withText(R.string.recipe_ingredients)));
    }

    @Test
    public void showRecyclerView() {
        onView(Matchers.allOf(withId(R.id.name_textView), withText(TEST_SALT_NAME)));
        onView(Matchers.allOf(withId(R.id.measure_textView), withText(TEST_MEASURE_NAME)));
        onView(Matchers.allOf(withId(R.id.quantity_textView), withText(TEST_QUANTITY_NAME)));
    }
}
