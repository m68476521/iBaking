package com.m68476521.mike.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.fragments.IngredientFragment;
import com.m68476521.mike.baking.fragments.RecipeFragment;
import com.m68476521.mike.baking.fragments.RecipeStepDetailFragment;
import com.m68476521.mike.baking.fragments.SingleFragmentActivity;

public class RecipeActivity extends SingleFragmentActivity implements RecipeFragment.CallBacks {
    private static final String ARG_RECIPE_NAME = "recipe_name";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private int lastSelectedFragment;

    public static Intent newIntent(Context packageContext, String recipeName, String id) {
        Intent intent = new Intent(packageContext, RecipeActivity.class);
        intent.putExtra(ARG_RECIPE_NAME, recipeName);
        intent.putExtra(ARG_RECIPE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String ingredientName = getIntent().getStringExtra(ARG_RECIPE_NAME);
        String id = getIntent().getStringExtra(ARG_RECIPE_ID);
        return RecipeFragment.newInstance(ingredientName, id);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onStepSelected(int position, String id_recipe) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = StepActivity.newIntent(this, id_recipe, position);
            startActivity(intent);
        } else {
            if (position == -1) {
                if (lastSelectedFragment != -1) {
                    Fragment newDetail = IngredientFragment.newInstance(id_recipe);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, newDetail)
                            .commit();
                }
                lastSelectedFragment = -1;
            } else {
                Fragment newDetail = RecipeStepDetailFragment.newInstance(position, id_recipe);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
                lastSelectedFragment = position;
            }
        }
    }
}
