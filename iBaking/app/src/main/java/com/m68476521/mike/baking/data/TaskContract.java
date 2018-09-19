package com.m68476521.mike.baking.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Base contract to the ContentProvider
 */

public class TaskContract {
    public static final String AUTHORITY = "com.m68476521.mike.baking";
    //base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_RECIPES_STEP = "steps";
    public static final String PATH_RECIPES_INGREDIENTS = "ingredients";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final Uri CONTENT_URI_STEPS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES_STEP).build();

        public static final Uri CONTENT_URI_INGREDIENTS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES_INGREDIENTS).build();

        // Task table and column names
        public static final String TABLE_NAME = "recipe";
        public static final String TABLE_NAME_STEP = "step";
        public static final String TABLE_NAME_INGREDIENTS = "ingredients";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_INGREDIENT_NAME = "name";
        public static final String COLUMN_INGREDIENT_SERVING = "serving";
        public static final String COLUMN_INGREDIENT_IMAGE = "image";

        // For step
        public static final String COLUMN_STEP_ID_INGREDIENT = "id_ing";
        public static final String COLUMN_STEP_SORT_DESC = "sort_desc";
        public static final String COLUMN_STEP_DESC = "desc";
        public static final String COLUMN_STEP_IMAGE = "image";
        public static final String COLUMN_STEP_VIDEO = "video";

        // For ingredient
        public static final String COLUMN_INGREDIENT_ID_RECIPE = "id_recipe";
        public static final String COLUMN_INGREDIENT_NAME_RECIPE = "name_ing";
        public static final String COLUMN_INGREDIENT_MEASURE = "ing_measure";
        public static final String COLUMN_INGREDIENT_QUANTITY = "ing_quantity";
    }
}
