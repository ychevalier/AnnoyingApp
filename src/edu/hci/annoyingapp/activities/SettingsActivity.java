package edu.hci.annoyingapp.activities;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.services.AnnoyingService;
import edu.hci.annoyingapp.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private static final String TAG = SettingsActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	// In seconds.
	private static final int DIALOG_TIME = 20;
	
	private boolean mLaunchService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		mLaunchService = false;

		Button startBt = (Button) findViewById(R.id.activity_settings_start_button);
		if(startBt != null) {
			startBt.setOnClickListener(this);
		}

		Button stopBt = (Button) findViewById(R.id.activity_settings_stop_button);
		if(stopBt != null) {
			stopBt.setOnClickListener(this);
		}

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		mLaunchService = stopService();
	}
	
	@Override
	protected void onStop() {
		
		if(mLaunchService) {
			Calendar cal = Calendar.getInstance();
			Intent background = new Intent(this, AnnoyingService.class);

			// A service can be stop by the system, so we need to schedule it...
			AnnoyingApplication.mPendingIntent = PendingIntent.getService(this, 0,
					background, 0);
			AnnoyingApplication.mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			// Start every DIALOG_TIME seconds.
			AnnoyingApplication.mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					DIALOG_TIME * 1000, AnnoyingApplication.mPendingIntent);
		}
		
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_settings_start_button) {
			mLaunchService = true;
		} else if (v.getId() == R.id.activity_settings_stop_button) {
			stopService();
			mLaunchService = false;
		}
	}
	
	private boolean stopService() {
		if(AnnoyingApplication.mAlarmManager != null 
				&& AnnoyingApplication.mPendingIntent != null) {
			AnnoyingApplication.mAlarmManager.cancel(AnnoyingApplication.mPendingIntent);
			AnnoyingApplication.mAlarmManager = null;
			AnnoyingApplication.mPendingIntent = null;
			
			return true;
		} else {
			return false;
		}
	}

}
