package com.m68476521.mike.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper used for ContentProvider
 */

public class TaskDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "tasksDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_ID + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_SERVING + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_IMAGE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

        final String CREATE_TABLE_STEP = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME_STEP + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_STEP_ID_INGREDIENT + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_STEP_SORT_DESC + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_STEP_DESC + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_STEP_IMAGE + " TEXT NULL, " +
                TaskContract.TaskEntry.COLUMN_STEP_VIDEO + " TEXT NULL);";

        db.execSQL(CREATE_TABLE_STEP);

        final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME_INGREDIENTS + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_ID_RECIPE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_NAME_RECIPE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_INGREDIENT_QUANTITY + " TEXT NULL);";

        db.execSQL(CREATE_TABLE_INGREDIENTS);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME_STEP);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME_INGREDIENTS);
        onCreate(db);
    }
}

