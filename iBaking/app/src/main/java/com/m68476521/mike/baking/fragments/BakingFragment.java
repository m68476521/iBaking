package com.m68476521.mike.baking.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.activities.RecipeActivity;
import com.m68476521.mike.baking.adapters.RecipesAdapter;
import com.m68476521.mike.baking.data.TaskContract;
import com.m68476521.mike.baking.data.TaskModelLoader;
import com.m68476521.mike.baking.database.RecipeItem;
import com.m68476521.mike.baking.databinding.GridViewBinding;
import com.m68476521.mike.baking.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment for BakingActivity
 */

public class BakingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = BakingFragment.class.getSimpleName();

    private ArrayList<RecipeItem> recipes = new ArrayList<>();

    private RecipesAdapter recipesAdapter;
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;
    private Cursor cursorFirstTime;

    private GridViewBinding Binding;

    //TODO: ask the reviewer if there is a way to save cursor when onSaveInstance
    //TODO: use RxJava to sync data
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cursorFirstTime = getActivity().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_ID);
        if (cursorFirstTime == null || cursorFirstTime.getCount() == 0) {
            getRecipesFromURL();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.grid_view, container, false);
        Binding.progressBar.setVisibility(ProgressBar.VISIBLE);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            int numberColumns = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numberColumns = 3;
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numberColumns,
                    GridLayoutManager.VERTICAL, false);
            Binding.recipesGrid.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                    GridLayoutManager.VERTICAL, false);
            Binding.recipesGrid.setLayoutManager(linearLayoutManager);
        }

        recipesAdapter = new RecipesAdapter(getActivity(), (view, position) -> {
            Log.d("- - - - ", " - - - -");
            cursorFirstTime.moveToPosition(position);
            Intent intent = RecipeActivity.newIntent(getContext(),
                    cursorFirstTime.getString(cursorFirstTime.getColumnIndex(
                            TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME)),
                    cursorFirstTime.getString(cursorFirstTime.getColumnIndex(
                            TaskContract.TaskEntry.COLUMN_ID)));

            Context context = view.getContext();
            context.startActivity(intent);
        });
        if (cursorFirstTime != null) {
            setupAdapterByCursor(cursorFirstTime);
            Binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        return Binding.getRoot();
    }

    private void getRecipesFromURL() {
        URL searchUrl = NetworkUtils.buildUrl("recipes", " ");
        new GetTopQueryTask().execute(searchUrl);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {
        return new TaskModelLoader(getActivity(), 2, "");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        cursorFirstTime = data;
        if (cursor != null) {
            setupAdapterByCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class GetTopQueryTask extends AsyncTask<URL, Void, ArrayList<RecipeItem>> {
        @Override
        protected ArrayList<RecipeItem> doInBackground(URL... params) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                recipes = networkUtils.getRecipes();
                return recipes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeItem> newRecipeItems) {
            if (recipes != null && recipes.size() > 0) {
                recipes = newRecipeItems;
                Log.d(TAG, " postExecute");
                setupAdapter();
            } else {
                Snackbar snackbar = Snackbar
                        .make(Binding.getRoot(), R.string.not_internet_connection, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            Binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setupAdapter() {
        //TODO: use RxJava
        if (cursor == null) {
            for (int i = 0; i < recipes.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TaskContract.TaskEntry.COLUMN_ID, recipes.get(i).getId());
                contentValues.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME, recipes.get(i).getIngredientName());
                contentValues.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_SERVING, recipes.get(i).getServing());
                contentValues.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_IMAGE, recipes.get(i).getImage());
                getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

                for (int j = 0; j < recipes.get(i).getSteps().size(); j++) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put(TaskContract.TaskEntry.COLUMN_STEP_ID_INGREDIENT, recipes.get(i).getId());
                    contentValues2.put(TaskContract.TaskEntry.COLUMN_STEP_SORT_DESC, recipes.get(i).getSteps().get(j).getSortDescription());
                    contentValues2.put(TaskContract.TaskEntry.COLUMN_STEP_DESC, recipes.get(i).getSteps().get(j).getDescription());
                    contentValues2.put(TaskContract.TaskEntry.COLUMN_STEP_IMAGE, recipes.get(i).getSteps().get(j).getImage());
                    contentValues2.put(TaskContract.TaskEntry.COLUMN_STEP_VIDEO, recipes.get(i).getSteps().get(j).getVideo());
                    getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI_STEPS, contentValues2);
                }
                for (int j = 0; j < recipes.get(i).getIngredients().size(); j++) {
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_ID_RECIPE, recipes.get(i).getId());
                    contentValues3.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME_RECIPE, recipes.get(i).getIngredients().get(j).getIngredient());
                    contentValues3.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_MEASURE, recipes.get(i).getIngredients().get(j).getMeasure());
                    contentValues3.put(TaskContract.TaskEntry.COLUMN_INGREDIENT_QUANTITY, recipes.get(i).getIngredients().get(j).getQuantity());
                    getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI_INGREDIENTS, contentValues3);
                }
            }

            if (cursor == null) {
                Log.d(TAG, "calling ContentProvider");
                getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this).forceLoad();
            }
        }
    }

    private void setupAdapterByCursor(Cursor cursor) {
        recipesAdapter.swapCursor(cursor);
        Binding.recipesGrid.setAdapter(recipesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
}
