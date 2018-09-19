package com.m68476521.mike.baking.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.TaskModelLoader;

/**
 * This class is a PagerView for steps
 */

public class RecipeDetailPagerFragment extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_RECIPE_POSITION = "recipe_position";
    private ViewPager viewPager;
    private int positionRecipe;
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);

        recipeId = (int) getIntent().getSerializableExtra(ARG_RECIPE_ID);
        positionRecipe = (int) getIntent().getSerializableExtra(ARG_RECIPE_POSITION);
        viewPager = findViewById(R.id.activity_detail_pager_view);
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TaskModelLoader(this, 1, Integer.toString(recipeId));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        setupViewer();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setupViewer() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return RecipeStepDetailFragment.newInstance(position, Integer.toString(positionRecipe));
            }

            @Override
            public int getCount() {
                if (cursor != null) {
                    return cursor.getCount();
                } else {
                    return 0;
                }

            }
        });
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(positionRecipe);
    }
}
