package com.m68476521.mike.baking.fragments;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.adapters.IngredientAdapter;
import com.m68476521.mike.baking.data.TaskModelLoader;
import com.m68476521.mike.baking.databinding.ActivityRecipeBinding;
import com.squareup.picasso.Picasso;

/**
 * Fragment: IngredientFragment shows a recycler view of ingredients
 */

public class IngredientFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_RECIPE_NAME = "recipe_name";
    private static final String ARG_RECIPE_INGREDIENT_ID = "recipe_id";

    private Context context;
    private ActivityRecipeBinding Binding;
    private static final int TASK_LOADER_ID = 0;
    private Cursor cursor;
    private IngredientAdapter ingredientAdapter;
    private String id_recipe;
    private boolean isViewCreated = false;

    public static IngredientFragment newInstance(String id) {
        Bundle args = new Bundle();

        args.putString(ARG_RECIPE_INGREDIENT_ID, id);

        IngredientFragment ingredientFragment = new IngredientFragment();
        ingredientFragment.setArguments(args);
        return ingredientFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        id_recipe = getArguments().getString(ARG_RECIPE_INGREDIENT_ID);
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.activity_recipe, container, false);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            Picasso.with(getActivity())
                    .load(R.drawable.ingredients)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(Binding.backdropImage);

            assert Binding.titleRecipe != null;
            Binding.titleRecipe.setText(R.string.recipe_ingredients);
            assert Binding.subtitleTextView != null;
            Binding.subtitleTextView.setText(R.string.ingredients_subtitle);
            assert Binding.toolbarNote != null;
            Binding.toolbarNote.setNavigationIcon(R.mipmap.ic_back);
            ((AppCompatActivity) getActivity()).setSupportActionBar(Binding.toolbarNote);

            assert Binding.collapsingToolbar != null;
            Binding.collapsingToolbar.setTitle(" ");
            Binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

            Binding.collapsingToolbar.setExpandedTitleColor(Color.WHITE);
            assert Binding.appbar != null;
            Binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        Binding.collapsingToolbar.setTitle("Ingredients");
                        isShow = true;
                    } else if (isShow) {
                        Binding.collapsingToolbar.setTitle(" ");
                        isShow = false;
                    }
                }
            });
        }
        assert Binding.galleryRecyclerView != null;
        Binding.specialTitle.getRoot().setVisibility(View.GONE);
        Binding.galleryRecyclerView.recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        Binding.galleryRecyclerView.recyclerView.setLayoutManager(mLayoutManager);

        Binding.galleryRecyclerView.recyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientAdapter = new IngredientAdapter();

        Binding.galleryRecyclerView.recyclerView.setAdapter(ingredientAdapter);
        isViewCreated = true;
        if (cursor != null) {
            ingredientAdapter.swapCursor(cursor);
        }
        return Binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_RECIPE_NAME, "MIKE test");
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TaskModelLoader(getActivity(), 3, id_recipe);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        if (isViewCreated) {
            ingredientAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
