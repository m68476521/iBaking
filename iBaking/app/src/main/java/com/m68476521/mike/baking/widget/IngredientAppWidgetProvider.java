package com.m68476521.mike.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.m68476521.mike.baking.R;
import com.m68476521.mike.baking.activities.BakingActivity;
import com.m68476521.mike.baking.data.IngredientsService;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientAppWidgetProvider extends AppWidgetProvider {

    private static final String ARG_RECIPE_INGREDIENT_ID = "recipe_id";
    private static String mId;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String id,
                                        int appWidgetId) {
        mId = id;
        CharSequence widgetTextRecipeName = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_app_widget);
        views.setTextViewText(R.id.appwidget_text_name, widgetTextRecipeName);

        Intent intent = new Intent(context, BakingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text_name, pendingIntent);

        Intent widgetServiceIntent = new Intent(context, WidgetService.class);
        widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        widgetServiceIntent.putExtra(ARG_RECIPE_INGREDIENT_ID, id);

        Bundle extras = new Bundle();
        extras.putString(ARG_RECIPE_INGREDIENT_ID, id);

        widgetServiceIntent.putExtras(extras);
        widgetServiceIntent.setData(Uri.parse(widgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.ingredients_list_view, widgetServiceIntent);
        views.setEmptyView(R.id.ingredients_list_view, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        IngredientsService.startActionUpdatePlantWidgets(context, "1");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, mId, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                String id, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id, appWidgetId);
        }
    }
}

