package com.m68476521.mike.baking.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.TaskContract;

/**
 * Adapter: in order to show a list of ingredients this adapter might be use, it has name, quantity
 * and measure of the ingredient
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private Cursor cursor;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView ingredientNameTextView;
        public final TextView ingredientMeasureTextView;
        public final TextView ingredientQuantityTextView;

        public ViewHolder(View view) {
            super(view);
            ingredientNameTextView = view.findViewById(R.id.name_textView);
            ingredientMeasureTextView = view.findViewById(R.id.measure_textView);
            ingredientQuantityTextView = view.findViewById(R.id.quantity_textView);
        }
    }

    public IngredientAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int indexName = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME_RECIPE);
        String ingredientName = cursor.getString(indexName);
        holder.ingredientNameTextView.setText(ingredientName);

        int indexMeasure = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_MEASURE);
        String ingredientMeasure = cursor.getString(indexMeasure);
        holder.ingredientMeasureTextView.setText(ingredientMeasure);

        int indexQuantity = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_INGREDIENT_QUANTITY);
        String ingredientQuantity = cursor.getString(indexQuantity);
        holder.ingredientQuantityTextView.setText(ingredientQuantity);
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == c) {
            return; // bc nothing has changed
        }

        this.cursor = c; // new cursor value assigned
        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
    }
}
