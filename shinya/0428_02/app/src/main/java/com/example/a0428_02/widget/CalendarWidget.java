package com.example.a0428_02.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.a0428_02.R;

public class CalendarWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // 複数ウィジェット対応
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // 日付フォーマット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (EEE)", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // RemoteViewsでレイアウト操作
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_calendar);
        views.setTextViewText(R.id.textViewDate, currentDate);

        // 更新
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
