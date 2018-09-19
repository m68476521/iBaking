package com.m68476521.mike.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.m68476521.mike.baking.fragments.IngredientFragment;
import com.m68476521.mike.baking.fragments.SingleFragmentActivity;

/**
 * Activity: IngredientActivity for IngredientFragment
 */

public class IngredientActivity extends SingleFragmentActivity {
    private static final String ARG_RECIPE_INGREDIENT_ID = "recipe_id";

    public static Intent newIntent(Context packageContent, String id) {
        Intent intent = new Intent(packageContent, IngredientActivity.class);
        intent.putExtra(ARG_RECIPE_INGREDIENT_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String id = getIntent().getStringExtra(ARG_RECIPE_INGREDIENT_ID);
        return IngredientFragment.newInstance(id);
    }
}
