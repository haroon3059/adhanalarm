package islam.adhanalarm.widget;

import islam.adhanalarm.AdhanAlarm;
import islam.adhanalarm.R;
import islam.adhanalarm.Schedule;
import islam.adhanalarm.VARIABLE;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TimetableWidgetProvider extends AppWidgetProvider {
	private static final int[] times = new int[]{R.id.fajr, R.id.sunrise, R.id.dhuhr, R.id.asr, R.id.maghrib, R.id.ishaa, R.id.next_fajr};
	private static final int[] am_pms = new int[]{R.id.fajr_am_pm, R.id.sunrise_am_pm, R.id.dhuhr_am_pm, R.id.asr_am_pm, R.id.maghrib_am_pm, R.id.ishaa_am_pm, R.id.next_fajr_am_pm};
	private static final int[] markers = new int[]{R.id.fajr_marker, R.id.sunrise_marker, R.id.dhuhr_marker, R.id.asr_marker, R.id.maghrib_marker, R.id.ishaa_marker, R.id.next_fajr_marker};

	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");
	private static final SimpleDateFormat amPmFormat = new SimpleDateFormat("a");

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		setLatestTimetable(context, appWidgetManager, appWidgetIds);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void setLatestTimetable(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TimetableWidgetProvider.class));
		setLatestTimetable(context, appWidgetManager, appWidgetIds);
	}
	private static void setLatestTimetable(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		if(VARIABLE.settings == null) VARIABLE.settings = context.getSharedPreferences("settingsFile", Context.MODE_PRIVATE);

		final GregorianCalendar[] schedule = Schedule.today().getTimes();
		for(int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_timetable);

			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AdhanAlarm.class), 0);
			views.setOnClickPendingIntent(R.id.widget_timetable, pendingIntent);

			for(int j = 0; j < times.length; j++) {
				views.setTextViewText(times[j], timeFormat.format(schedule[j].getTime()));
				views.setTextViewText(am_pms[j], amPmFormat.format(schedule[j].getTime()));
				views.setTextViewText(markers[j], j == Schedule.today().nextTimeIndex() ? context.getString(R.string.next_time_marker_reverse) : "");
			}
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
	}
}