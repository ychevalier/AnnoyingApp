package edu.hci.annoyingapp.protocol;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.utils.Common;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PushMessages {
	
	public static final String EXTRA_RUN = "EXTRA_RUN";
	
	public static final String EXTRA_CONDITION = "EXTRA_CONDITION";
	
	public static final String EXTRA_POSITIVE = "EXTRA_POSITIVE";
	
	public static final String EXTRA_NEGATIVE = "EXTRA_NEGATIVE";
	
	public static final String EXTRA_DIALOG = "EXTRA_DIALOG";
	
	public static final String EXTRA_LITTLE_INTERVAL = "EXTRA_LITTLE_INTERVAL";
	
	public static final String EXTRA_BIG_INTERVAL = "EXTRA_BIG_INTERVAL";
	
	public static final String EXTRA_SURVEY = "EXTRA_SURVEY";
	
	public static final String EXTRA_TITLE = "EXTRA_TITLE";
	
	public static final String EXTRA_DATA_INTERVAL = "EXTRA_DATA_INTERVAL";
	
	/**
	 * Save params in Shared Prefs and return a Survey URL if there is one.
	 * @param context
	 * @param b
	 * @return
	 */
	public static final String saveParams(Context context, Bundle b) {
		
		boolean running = Common.DEFAULT_BOOL;
		
		int littleInterval = -1;
		int bigInterval = -1;
		int condition = -1;
		int dataInterval = -1;
		
		String title = b.getString(EXTRA_TITLE);
		String positive = b.getString(EXTRA_POSITIVE);
		String negative = b.getString(EXTRA_NEGATIVE);
		String text = b.getString(EXTRA_DIALOG);
		
		String tmp = b.getString(EXTRA_RUN);
		if(tmp != null) {
			running = Boolean.parseBoolean(tmp);
		}
		
		tmp = b.getString(EXTRA_LITTLE_INTERVAL);
		if(tmp != null) {
			try {
				littleInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = b.getString(EXTRA_BIG_INTERVAL);
		if(tmp != null) {
			try {
				bigInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = b.getString(EXTRA_DATA_INTERVAL);
		if(tmp != null) {
			try {
				dataInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = b.getString(EXTRA_CONDITION);
		if(tmp != null) {
			try {
				condition = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}

		// Save the prefs.
		SharedPreferences settings = context.getSharedPreferences(
				Common.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putBoolean(Common.PREF_IS_SERVICE_RUNNING, running);
		
		if(condition != -1) {
			editor.putInt(Common.PREF_CONDITION, condition);
		}
		if(bigInterval != -1) {
			editor.putInt(Common.PREF_BIG_INTERVAL, bigInterval);
		}
		if(littleInterval != -1) {
			editor.putInt(Common.PREF_LITTLE_INTERVAL, littleInterval);
		}
		if(dataInterval != -1) {
			editor.putInt(Common.PREF_DATA_INTERVAL, dataInterval);
			AnnoyingApplication.startDataService(context, dataInterval);
		}
		//if(positive != null) {
		//	editor.putString(Common.PREF_, positive);
		//}
		//if(negative != null) {
		//	editor.putString(Common.PREF_NEGATIVE_BUTTON, negative);
		//}
		if(text != null) {
			editor.putString(Common.PREF_DIALOG_TEXT, text);
		}
		if(title != null) {
			editor.putString(Common.PREF_DIALOG_TITLE, title);
		}
		editor.commit();
		
		if(running) {
			int inter = settings.getInt(Common.PREF_BIG_INTERVAL, Common.DEFAULT_INT);
			AnnoyingApplication.startService(context, inter);
		} else {
			AnnoyingApplication.stopService(context);
		}
		
		return b.getString(EXTRA_SURVEY);
	}
}
