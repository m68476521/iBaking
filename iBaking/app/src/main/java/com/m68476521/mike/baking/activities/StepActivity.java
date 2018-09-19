package com.m68476521.mike.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.m68476521.mike.baking.fragments.RecipeStepDetailFragment;
import com.m68476521.mike.baking.fragments.SingleFragmentActivity;

/**
 * Activity to display particular step of the recipe
 */

public class StepActivity extends SingleFragmentActivity {
    private static final String ARG_RECIPE_INGREDIENT_ID = "recipe_id";
    private static final String ARG_RECIPE_POSITION = "recipe_position";

    public static Intent newIntent(Context packageContent, String id, int position) {
        Intent intent = new Intent(packageContent, StepActivity.class);
        intent.putExtra(ARG_RECIPE_INGREDIENT_ID, id);
        intent.putExtra(ARG_RECIPE_POSITION, position);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String id = getIntent().getStringExtra(ARG_RECIPE_INGREDIENT_ID);
        int position = getIntent().getIntExtra(ARG_RECIPE_POSITION, 0);
        return RecipeStepDetailFragment.newInstance(position, id);
    }
}
