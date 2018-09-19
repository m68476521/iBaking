package com.m68476521.mike.baking.adapters;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.TaskContract;
import com.m68476521.mike.baking.databinding.CartRecipeBinding;

/**
 * Recipe parent adapter
 */

public class RecipeCartAdapter extends RecyclerView.Adapter<RecipeCartAdapter.ViewHolder> {
    private static final String TAG = RecipeCartAdapter.class.getSimpleName();
    private int row_index;
    private Cursor cursor;

    private final OnItemClicked listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final CardView cardView;

        private final CartRecipeBinding Binding;

        public ViewHolder(View view) {
            super(view);
            Binding = DataBindingUtil.bind(view);
            textView = view.findViewById(R.id.recipe_name_tv);
            cardView = view.findViewById(R.id.card_view);
        }

        public CartRecipeBinding getBinding() {
            return Binding;
        }
    }

    // TODO: change this to be more flexible
    public RecipeCartAdapter(int position, OnItemClicked listener) {
        this.listener = listener;
        row_index = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_recipe, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, final int position) {
        cursor.moveToPosition(position);
        int indexSortDesc = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STEP_SORT_DESC);
        String sortDesc = cursor.getString(indexSortDesc);

        holder.textView.setText(sortDesc);
        holder.itemView.setOnClickListener(view -> {
            Log.d(TAG, "MIKE CLICKED");
            listener.onItemClick(view, position);
            row_index = position;
            notifyDataSetChanged();
        });

        if (row_index == position) {
            holder.cardView.setBackgroundColor(Color.parseColor("#567845"));
            holder.textView.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#FFD180"));
            holder.textView.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
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
