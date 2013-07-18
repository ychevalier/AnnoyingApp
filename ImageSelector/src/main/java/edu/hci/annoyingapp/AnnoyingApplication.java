package edu.hci.annoyingapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import edu.hci.annoyingapp.services.AnnoyingService;
import edu.hci.annoyingapp.services.DataSenderService;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingApplication extends Application {

    public static final boolean DEBUG_MODE = true;

    // Little trick so that every instance
    // of SettingsActivity can access the alarm...
    private static PendingIntent mDialogIntent;
    private static PendingIntent mDataIntent;

    private static boolean mIsDialogRunning;

	private static int mNbDisplay;
	private static int mDayOfMonth;

    public static String[] IMAGES = {
            "anchor",
            "ant",
            "apple",
            "arm",
            "arrow",
            "ashtray",
            "ball",
            "balloon",
            "banana",
            "bed",
            "bell",
            "belt",
            "bench",
            "bird",
            "book",
            "bottle",
            "butterfly",
            "button",
            "camel",
            "candle",
            "car",
            "carrot",
            "cat",
            "caterpillar",
            "chisel",
            "cigarette",
            "clock",
            "cloud",
            "comb",
            "cow",
            "crown",
            "desk",
            "dog",
            "door",
            "dress",
            "drum",
            "duck",
            "ear",
            "elephant",
            "envelope",
            "eye",
            "fence",
            "finger",
            "fish",
            "flag",
            "flower",
            "fly",
            "foot",
            "fork",
            "fox",
            "giraffe",
            "glove",
            "goat",
            "gorilla",
            "grapes",
            "guitar",
            "hammer",
            "hand",
            "harp",
            "heart",
            "helicopter",
            "horse",
            "kangaroo",
            "key",
            "kite",
            "ladder",
            "leaf",
            "leg",
            "lemon",
            "lion",
            "lobster",
            "monkey",
            "mouse",
            "nail",
            "nose",
            "onion",
            "orange",
            "owl",
            "peacock",
            "pear",
            "pen",
            "pencil",
            "penguin",
            "pepper",
            "piano",
            "pig",
            "pineapple",
            "pipe",
            "plane",
            "pliers",
            "pumpkin",
            "rabbit",
            "ring",
            "rubber",
            "ruler",
            "saw",
            "scissors",
            "screw",
            "screwdriver",
            "seahorse",
            "sheep",
            "shirt",
            "shoe",
            "skirt",
            "skunk",
            "snail",
            "snake",
            "snowman",
            "sock",
            "spoon",
            "squirrel",
            "star",
            "stool",
            "sun",
            "swan",
            "swing",
            "table",
            "tie",
            "tiger",
            "toaster",
            "toothbrush",
            "tortoise",
            "train",
            "tree",
            "trousers",
            "trumpet",
            "umbrella",
            "vase",
            "violin",
            "watch",
            "whistle",
            "windmill",
            "window",
            "zebra"};

    @Override
    public void onCreate() {
        super.onCreate();
        mDialogIntent = null;
        mDataIntent = null;

        mIsDialogRunning = false;

		mDayOfMonth = 0;
		mNbDisplay = 0;
    }

	public static boolean canIDisplayMoreDialogs(int dayOfMonth) {
		if(mDayOfMonth != dayOfMonth) {
			mDayOfMonth = dayOfMonth;
			mNbDisplay = 0;
			return true;
		} else if(mNbDisplay < Common.MAX_DISPLAY_PER_DAY) {
			return true;
		} else {
			return false;
		}
	}

    public static void startDialog() {
        mIsDialogRunning = true;
		mNbDisplay++;
    }

    public static void stopDialog() {
        mIsDialogRunning = false;
    }

    public static boolean isDialogStarted() {
        return mIsDialogRunning;
    }

    public static void startService(Context context, int secDialog) {
        stopService(context);

        Intent backgroundDialogService = new Intent(context, AnnoyingService.class);

        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();

        // A service can be stop by the system, so we need to schedule it...
        mDialogIntent = PendingIntent.getService(context, 0,
                backgroundDialogService, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Start every DIALOG_TIME seconds.
        alarmManager.set(AlarmManager.RTC_WAKEUP, time + secDialog * 1000, mDialogIntent);
    }

    public static boolean stopService(Context context) {
        if (mDialogIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(mDialogIntent);
            mDialogIntent = null;
            return true;
        } else {
            return false;
        }
    }

    public static void startDataService(Context context, int secData) {
        stopDataService(context);

        Intent backgroundDataService = new Intent(context, DataSenderService.class);

        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();

        mDataIntent = PendingIntent.getService(context, 0, backgroundDataService, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time + secData * 1000, secData * 1000, mDataIntent);
    }

    public static boolean stopDataService(Context context) {
        if (mDataIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(mDataIntent);
            mDataIntent = null;
            return true;
        } else {
            return false;
        }
    }

    public static void launchStatusIcon(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.app_text));
        // Creates an explicit intent for an Activity in your app


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(Common.APP_NOTIF, mBuilder.build());
    }
}
