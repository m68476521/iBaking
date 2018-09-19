package com.m68476521.mike.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.data.TaskContract;

/**
 * This Provider is used to handle the listView item from the Widget
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = ListProvider.class.getSimpleName();
    private static final String ARG_RECIPE_INGREDIENT_ID = "recipe_id";
    private Context context = null;

    private Cursor cursor;

    private String mId = "";

    public ListProvider(Context context, Intent intent) {
        if (intent.hasExtra(ARG_RECIPE_INGREDIENT_ID)) {
            mId = intent.getStringExtra(ARG_RECIPE_INGREDIENT_ID);
        }
        this.context = context;
    }

    private void initCursor() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = TaskContract.TaskEntry.CONTENT_URI_INGREDIENTS;
        uri = uri.buildUpon().appendPath(mId).build();
        if (mId == null) {
            Log.d(TAG, "id is null");
            return;

        }
        cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        if (mId != null) {
            initCursor();
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        initCursor();
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.collection_widget_list_item);
        cursor.moveToPosition(position);

        String ingredientName = cursor.getString(cursor.getColumnIndex(
                TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME_RECIPE));

        String ingredientMeasure = cursor.getString(cursor.getColumnIndex(
                TaskContract.TaskEntry.COLUMN_INGREDIENT_MEASURE));

        String ingredientQuantity = cursor.getString(cursor.getColumnIndex(
                TaskContract.TaskEntry.COLUMN_INGREDIENT_QUANTITY));

        String ingredientFull = " " + ingredientName + ", " + ingredientMeasure + ": " + ingredientQuantity;
        remoteViews.setTextViewText(R.id.widgetItemTaskNameLabel, ingredientFull);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
