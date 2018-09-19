package com.m68476521.mike.baking.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.activities.IngredientActivity;
import com.m68476521.mike.baking.adapters.RecipeCartAdapter;
import com.m68476521.mike.baking.data.TaskModelLoader;
import com.m68476521.mike.baking.databinding.ActivityRecipeBinding;
import com.squareup.picasso.Picasso;

/**
 * Fragment, shows a recipe detail
 */

public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_RECIPE_NAME = "recipe_name";
    private static final String ARG_RECIPE_ID = "recipe_id";

    private static final String SELECTED_ITEM = "item_selected";
    private static final String INGREDIENT_NAME = "ingredient_name";

    private static final int TASK_LOADER_ID = 1;
    private Context context;
    private String recipeName;
    private String mId;
    private RecipeCartAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean twoPane = false;
    private CallBacks callBacks;
    private int itemId = 0;
    private ActivityRecipeBinding Binding;

    private Cursor cursor;

    public interface CallBacks {
        void onStepSelected(int position, String id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBacks = (CallBacks) context;
    }

    public static RecipeFragment newInstance(String recipeName, String id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_NAME, recipeName);
        args.putSerializable(ARG_RECIPE_ID, id);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
        if (savedInstanceState == null) {
            mId = getArguments().getString(ARG_RECIPE_ID);
            recipeName = getArguments().getString(ARG_RECIPE_NAME);
        } else {
            mId = savedInstanceState.getString(ARG_RECIPE_ID);
            itemId = savedInstanceState.getInt(SELECTED_ITEM);
            recipeName = savedInstanceState.getString(INGREDIENT_NAME);
        }
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.activity_recipe, container, false);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            twoPane = true;

            Picasso.with(getActivity())
                    .load(R.drawable.backimage)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(Binding.backdropImage);

            assert Binding.titleRecipe != null;
            Binding.titleRecipe.setText(recipeName);
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
                        Binding.collapsingToolbar.setTitle(recipeName);
                        isShow = true;
                    } else if (isShow) {
                        Binding.collapsingToolbar.setTitle(" ");
                        isShow = false;
                    }
                }
            });
        }
        assert Binding.galleryRecyclerView != null;
        Binding.galleryRecyclerView.recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        Binding.galleryRecyclerView.recyclerView.setLayoutManager(layoutManager);
        Binding.galleryRecyclerView.recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        Binding.galleryRecyclerView.recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState != null) {
            itemId = savedInstanceState.getInt(SELECTED_ITEM);
        }

        adapter = new RecipeCartAdapter(itemId, (view, position) -> {
            itemId = position;
            callBacks.onStepSelected(position, mId);
        });
        Binding.galleryRecyclerView.recyclerView.setAdapter(adapter);
        Binding.galleryRecyclerView.recyclerView.post(() -> {
            layoutManager.scrollToPosition(2);
            adapter.notifyItemChanged(itemId);
            adapter.notifyDataSetChanged();
        });

        if (!twoPane && itemId == 0) {
            callBacks.onStepSelected(0, mId);
        }

        Binding.specialTitle.ingredientsTitle.setText(R.string.ingredients_name);
        Binding.specialTitle.ingredientsTitle.setOnClickListener(view -> {
            if (!tabletSize) {
                Intent intent = IngredientActivity.newIntent(getActivity(), mId);
                startActivity(intent);
            } else {
                callBacks.onStepSelected(-1, mId);
            }
        });
        return Binding.getRoot();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, itemId);
        outState.putString(ARG_RECIPE_ID, mId);
        outState.putString(INGREDIENT_NAME, recipeName);
        super.onSaveInstanceState(outState);
    }

    private void setupAdapterByCursor(Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TaskModelLoader(getActivity(), 1, mId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        setupAdapterByCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


