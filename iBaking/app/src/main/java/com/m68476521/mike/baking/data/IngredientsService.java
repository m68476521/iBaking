package com.m68476521.mike.baking.data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.m68476521.mike.baking.widget.IngredientAppWidgetProvider;

/**
 * IntentService used to handle the provider update in the Widget
 */

public class IngredientsService extends IntentService {

    private static final String ACTION_INGREDIENTS_lIST =
            "com.m68476521.mike.baking.data.list_ingredients";

    private static final String ACTION_UPDATE_lIST =
            "com.m68476521.mike.baking.data.list_update";

    public IngredientsService() {
        super("IngredientsService");
    }

    /**
     * Starts this service to perform ingredientsList action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    private static String mId;

    public static void startActionIngredientsList(Context context) {
        Intent intent = new Intent(context, IngredientsService.class);
        intent.setAction(ACTION_INGREDIENTS_lIST);
        context.startService(intent);
    }

    /**
     * Starts this service to perform UpdateListIngredientWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdatePlantWidgets(Context context, String id) {
        Intent intent = new Intent(context, IngredientsService.class);
        intent.setAction(ACTION_UPDATE_lIST);
        mId = id;
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_lIST.equals(action)) {
                handleActionUpdateListWidgets();
            }
        }
    }

    private void handleActionUpdateListWidgets() {
        //Query to get the ingredients that's most in need for water (last watered)
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientAppWidgetProvider.class));
        //Now update all widgets
        IngredientAppWidgetProvider.updateIngredientsWidgets(this, appWidgetManager,
                mId, appWidgetIds);
    }
}
