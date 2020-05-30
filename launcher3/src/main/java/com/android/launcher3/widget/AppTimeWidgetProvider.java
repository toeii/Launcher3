package com.android.launcher3.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.android.launcher3.R;

/**
 * @author Toeii
 * @create 2020/5/27
 * @Describe
 */
public class AppTimeWidgetProvider extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if(action.equals("android.appwidget.action.APPWIDGET_UPDATE")){
                updateWidget(context);
            } else {
                super.onReceive(context, intent);
            }
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidget(context);
    }


    private void updateWidget(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        AppWidgetManager appwidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentname = new ComponentName(context, AppTimeWidgetProvider.class);
        appwidgetManager.updateAppWidget(componentname, remoteViews);
    }

}
