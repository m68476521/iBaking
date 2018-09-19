package com.m68476521.mike.baking.activities;

import android.support.v4.app.Fragment;

import com.m68476521.mike.baking.fragments.RecipeStepDetailFragment;
import com.m68476521.mike.baking.fragments.SingleFragmentActivity;

/**
 * Activity that shows a fragment for a detailed step to cook the recipes
 */

public class RecipeStepDetail extends SingleFragmentActivity {
    private static final String ARG_STEP_ID = "recipe_id";
    private static final String ARG_STEP_NUMBER = "recipe_number";

    @Override
    protected Fragment createFragment() {
        int stepNumber = getIntent().getIntExtra(ARG_STEP_NUMBER, 0);
        String steps = getIntent().getStringExtra(ARG_STEP_ID);
        return RecipeStepDetailFragment.newInstance(stepNumber, steps);
    }
}
