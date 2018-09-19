package com.m68476521.mike.baking.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Remote service for Widget list updates
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }
}
