package edu.hci.annoyingapp.activities;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.protocol.Receivers;
import edu.hci.annoyingapp.utils.Common;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	private ProgressDialog mDialog;
	private String mSurveyUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		SharedPreferences settings = getSharedPreferences(
				Common.PREFS_NAME, 0);
		
		int bigInterval = settings.getInt(Common.PREF_BIG_INTERVAL,
				-1);

		boolean isRunning = settings.getBoolean(
				Common.PREF_IS_SERVICE_RUNNING,
				false);
		
		int dataInterval = settings.getInt(Common.PREF_DATA_INTERVAL,
				-1);
		
		mSurveyUrl = settings.getString(Common.PREF_FIRST_SURVEY, null);
		
		String token = settings.getString(Common.PREF_TOKEN, null);
		
		mSurveyUrl += token;
		
		boolean firstTime = settings.getBoolean(Common.PREF_FIRST_TIME, true);
		
		if(mSurveyUrl != null && firstTime) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Common.PREF_FIRST_TIME, false);
			editor.commit();
			launchDemoSurvey();
		}
		
		Button survey = (Button) findViewById(R.id.activity_main_demo_survey);
		survey.setOnClickListener(this);
	
		if(isRunning) {
			AnnoyingApplication.startService(this, bigInterval);
		}
		
		launchStatusIcon();
		
		AnnoyingApplication.startDataService(this, dataInterval);
		
		registerReceiver(mHandleUnregistrationReceiver,new IntentFilter(Receivers.UNREGISTERED));
	}

	@Override
	protected void onStop() {

		super.onStop();
		// Little trick so that activity is not on background when dialog
		// shows up.
		finish();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mHandleUnregistrationReceiver);
		super.onDestroy();
	}

	private final BroadcastReceiver mHandleUnregistrationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			SharedPreferences settings = context.getSharedPreferences(
					Common.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Common.PREF_IS_SERVICE_RUNNING, false);
			editor.commit();
			
			AnnoyingApplication.stopService(context);
			
			Intent i = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
			mDialog.dismiss();
		}
	};
	
	private void launchStatusIcon() {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
				.setOngoing(true)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(getString(R.string.app_name))
		        .setContentText(getString(R.string.app_text));
		// Creates an explicit intent for an Activity in your app
		
		
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(Common.APP_NOTIF, mBuilder.build());
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.activity_main_demo_survey) {
			launchDemoSurvey();
		}
	}
	
	private void launchDemoSurvey() {
		Intent i = new Intent(this, SurveyActivity.class);
		i.putExtra(SurveyActivity.EXTRA_SURVEY, mSurveyUrl);
		startActivity(i);
	}
}
