package edu.hci.annoyingapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.SurveyActivity;

public class Common {

	// ================== Server properties ====================

	/**
	 * Base URL of the Server.
	 */
	public static final String SERVER_URL = "http://10.2.128.34:8080/annoying-server";

	/**
	 * Google API project id registered to use GCM.
	 */
	public static final String SENDER_ID = "454161920705";

	// ================== Shared Preferences ====================

	public static final String PREFS_NAME = "AnnoyingPrefs";

	public static final String PREF_UID = "uid";
	public static final String PREF_IS_SERVICE_RUNNING = "IsServiceRunning";
	public static final String PREF_CONDITION = "Condition";
	public static final String PREF_BIG_INTERVAL = "BigInterval";
	public static final String PREF_LITTLE_INTERVAL = "LittleInterval";
	public static final String PREF_POSITIVE_BUTTON = "PositiveButton";
	public static final String PREF_NEGATIVE_BUTTON = "NegativeButton";
	public static final String PREF_DIALOG_TEXT = "DialogText";
	public static final String PREF_LAST_SUCCESSFUL_SENDING = "LastSending";

	// This is the distant database, maybe put it in local db later...
	public static final int BUTTON_POSITIVE = 0;
	public static final int BUTTON_NEGATIVE = 1;
	public static final int BUTTON_OTHER = 2;

	// Yes/No before honeycomb and No/Yes later
	public static final int CONDITION_DEFAULT = 0;
	public static final int CONDITION_ALT = 1;
	public static final int CONDITION_OTHER = 2;

	// Default are not relevant anymore since those value are given
	// at registration.
	public static final boolean DEFAULT_IS_RUNNING = false;
	public static final int DEFAULT_BIG_INTERVAL = 20;
	public static final int DEFAULT_LITTLE_INTERVAL = 20;
	public static final int DEFAULT_CONDITION = CONDITION_DEFAULT;
	public static final String DEFAULT_POSITIVE = "Yes";
	public static final String DEFAULT_NEGATIVE = "No";
	public static final String DEFAULT_DIALOG = "Would you like to close this dialog?";
	
	// Notification Id
	public static final int NOTIF_ID = 42;
	
	public static void launchSurveyNotification(Context context, String survey) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
				.setAutoCancel(true)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(context.getString(R.string.app_name))
		        .setContentText(context.getString(R.string.notification_text));
		// Creates an explicit intent for an Activity in your app
		
		Intent resultIntent = new Intent(context, SurveyActivity.class);
		resultIntent.putExtra(SurveyActivity.EXTRA_SURVEY, survey);
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(SurveyActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(Common.NOTIF_ID, mBuilder.build());
	}
}
