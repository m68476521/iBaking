package com.m68476521.mike.baking.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.IngredientsService;
import com.m68476521.mike.baking.data.TaskContract;
import com.m68476521.mike.baking.databinding.RecipeListItemBinding;
import com.squareup.picasso.Picasso;

/**
 * This class in an adapter for the gridView in Recipes portrait screen
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private final Context context;
    private Cursor cursor;

    private final OnItemClicked listener;

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewRecipeName;
        public final TextView textViewRecipeServing;
        public final ImageView imageViewFav;
        public final ImageView imageViewBackGround;

        private final RecipeListItemBinding Binding;

        public ViewHolder(View view) {
            super(view);
            Binding = DataBindingUtil.bind(view);
            textViewRecipeName = view.findViewById(R.id.recipe_name);
            textViewRecipeServing = view.findViewById(R.id.recipe_serving);
            imageViewFav = view.findViewById(R.id.fav_image_view);
            imageViewBackGround = view.findViewById(R.id.image_view_background);
        }

        public RecipeListItemBinding getBinding() {
            return Binding;
        }
    }

    public RecipesAdapter(Activity context, OnItemClicked listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int index = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME);
        String recipeName = cursor.getString(index);
        holder.textViewRecipeName.setText(recipeName);
        Log.d("MIKE", "MIKE::" + recipeName);

        int indexServing = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_SERVING);
        String serving = cursor.getString(indexServing);
        holder.textViewRecipeServing.setText(serving);

        int indexId = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_ID);
        final String id = cursor.getString(indexId);

        Picasso.with(context)
                .load(R.drawable.ic_favorite_border)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageViewFav);

        holder.imageViewFav.setOnClickListener(v -> IngredientsService.startActionUpdatePlantWidgets(context, id));

        String image = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_IMAGE));
        if (TextUtils.isEmpty(image)) {
            holder.imageViewBackGround.setVisibility(View.GONE);
        } else {
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.imageViewBackGround);
        }

        holder.itemView.setOnClickListener(view -> {
            Log.d("MIKE clicked::", Integer.toString(position));
            listener.onItemClick(view, position);
            notifyDataSetChanged();
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor cursor) {
        if (this.cursor == cursor) {
            return;
        }
        this.cursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
    }
}

