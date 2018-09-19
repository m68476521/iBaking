package com.m68476521.mike.baking.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ContentProvider for get all recipes, and insert new ones
 */

public class TaskContentProvider extends ContentProvider {
    private static final int TASKS = 100;
    private static final int TASK_WITH_ID = 101;

    private static final int STEPS = 200;
    private static final int STEPS_WITH_ID = 201;

    private static final int INGREDIENTS = 300;
    private static final int INGREDIENTS_WITH_ID = 301;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_RECIPES, TASKS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.BASE_CONTENT_URI + "/#", TASK_WITH_ID);

        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_RECIPES_STEP, STEPS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_RECIPES_STEP + "/#", STEPS_WITH_ID);

        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_RECIPES_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_RECIPES_INGREDIENTS + "/#", INGREDIENTS_WITH_ID);
        return uriMatcher;
    }

    private TaskDbHelper taskDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        taskDbHelper = new TaskDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = taskDbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case TASKS:
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEPS:
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME_STEP,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEPS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "id_ing=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME_STEP,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS:
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS_WITH_ID:
                String id_ing = uri.getPathSegments().get(1);
                String mSelection_ing = "id_recipe=?";
                String[] mSelectionArgs_ing = new String[]{id_ing};
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        mSelection_ing,
                        mSelectionArgs_ing,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS:
                long id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            case STEPS:
                long id_step = db.insert(TaskContract.TaskEntry.TABLE_NAME_STEP, null, contentValues);
                if (id_step > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI_STEPS, id_step);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            case INGREDIENTS:
                long id_ing = db.insert(TaskContract.TaskEntry.TABLE_NAME_INGREDIENTS, null, contentValues);
                if (id_ing > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI_INGREDIENTS, id_ing);
                } else {
                    throw new android.database.SQLException("Failed to insert raw " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
