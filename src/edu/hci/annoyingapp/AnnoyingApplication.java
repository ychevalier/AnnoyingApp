package edu.hci.annoyingapp;

import java.util.LinkedList;
import java.util.List;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import edu.hci.annoyingapp.model.Stat;

public class AnnoyingApplication extends Application {
	
	public static final boolean DEBUG_MODE = true;
	
	// Little trick so that every instance 
	// of SettingsActivity can access the alarm...
	public static AlarmManager mAlarmManager;
	public static PendingIntent mPendingIntent;
	
	private static boolean mIsDialogRunning;
	private static List<Stat> mDataList;
	
	@Override
	public void onCreate() {
		super.onCreate();

		mAlarmManager = null;
		mPendingIntent = null;
		
		mIsDialogRunning = false;
		
		mDataList = new LinkedList<Stat>();
	}
	
	public static void startDialog() {
		mIsDialogRunning = true;
	}
	
	public static void stopDialog(Stat d) {
		mIsDialogRunning = false;
		mDataList.add(d);
	}
	
	public static boolean isDialogStarted() {
		return mIsDialogRunning;
	}
	
	public static Stat[] getStats() {
		Stat[] ret = new Stat[mDataList.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = mDataList.get(i);
		}
		return ret;
	}
}
