package edu.hci.annoyingapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Random;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.ui.activities.SurveyActivity;

public class Common {


	// ================== Basic properties ====================

	public static final int MAX_DISPLAY_PER_DAY = 10;

	// ================== Server properties ====================

	/**
	 * Base URL of the Server.
	 */
	//public static final String SERVER_URL = "http://10.2.56.53:8080/annoying-server";
	public static final String SERVER_URL = "http://studentweb.cs.bham.ac.uk/yxc238/annoying-server";

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
	public static final String PREF_IMAGE = "Image";
	public static final String PREF_POSITION = "Position";
	public static final String PREF_THEME = "Theme";
	public static final String PREF_DIALOG_TEXT = "DialogText";
	public static final String PREF_DIALOG_TITLE = "DialogTitle";
	public static final String PREF_DATA_INTERVAL = "DataInterval";
	public static final String PREF_LAST_SUCCESSFUL_SENDING = "LastSending";
	public static final String PREF_TOKEN = "Token";
	public static final String PREF_FIRST_SURVEY = "FirstSurvey";
	public static final String PREF_FIRST_TIME = "FirstTime";

	public static final int THEME_DARK = 0;
	public static final int THEME_LIGHT = 1;

	// This is the distant database, maybe put it in local db later...
	public static final int POSITION_TOP = 0;
	public static final int POSITION_BOTTOM = 1;
	public static final int POSITION_OTHER = 2;

	// Yes/No before honeycomb and No/Yes later
	public static final int CONDITION_RANDOM = 0;
	public static final int CONDITION_POSITION = 1;
	public static final int CONDITION_ANSWER = 2;
	public static final int CONDITION_BOTH = 3;

	public static final String DEFAULT_TITLE = "ImageSelector";
	public static final String DEFAULT_MESSAGE = "Please click on ";

	public static final int DEFAULT_BIG_INTERVAL = 3600;
	public static final int DEFAULT_LITTLE_INTERVAL = 60;
	public static final int DEFAULT_DATA_INTERVAL = 3600;

	// Notification Id
	public static final int NOTIF_ID = 42;
	public static final int APP_NOTIF = 43;

	public static int getRandomImage() {
		return getRandomImage(-1);
	}

	public static int getRandomImage(int notThisImage) {
		Random randomGenerator = new Random();
		int randomInt;
		do {
			randomInt = randomGenerator.nextInt(AnnoyingApplication.IMAGES.length);
		} while (notThisImage != -1 && randomInt == notThisImage);

		return randomInt;
	}

	public static String getImageName(int image) {
		if (image >= 0 && image < AnnoyingApplication.IMAGES.length) {
			return AnnoyingApplication.IMAGES[image];
		}
		return null;
	}

	public static int getResId(Context c, int image) {
		String imageName = getImageName(image);
		if (imageName != null) {
			int res = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());
			if (res != 0) {
				return res;
			} else {
				return -1;
			}
		}
		return -1;
	}

	public static void launchSurveyNotification(Context context, String survey) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(context.getString(R.string.notification_text))
				.setDefaults(Notification.DEFAULT_ALL);
		// Creates an explicit intent for an Activity in your app

		//Intent resultIntent = new Intent(context, SurveyActivity.class);
		//resultIntent.putExtra(SurveyActivity.EXTRA_SURVEY, survey);

		Intent resultIntent = new Intent(Intent.ACTION_VIEW);
		resultIntent.setData(Uri.parse(survey));

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(SurveyActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(Common.NOTIF_ID, mBuilder.build());
	}
}
