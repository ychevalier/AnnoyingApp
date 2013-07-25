package edu.hci.annoyingapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bugsense.trace.BugSenseHandler;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.io.ExportDatabaseFileTask;
import edu.hci.annoyingapp.utils.Common;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	private String mSurveyUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(this, Common.BUGSENSE_KEY);

		setContentView(R.layout.activity_main);

		// == UI == //

		Button survey = (Button) findViewById(R.id.activity_main_demo_survey);
		survey.setOnClickListener(this);

		// TODO: Remove for deployment.
		Button export = (Button) findViewById(R.id.activity_main_export_db);
		if (DEBUG_MODE) {
			export.setVisibility(View.VISIBLE);
			export.setOnClickListener(this);
		} else {
			export.setVisibility(View.GONE);
		}

		// == Services == //

		SharedPreferences settings = getSharedPreferences(
				Common.PREFS_NAME, 0);

		int bigInterval = settings.getInt(Common.PREF_BIG_INTERVAL,
				Common.DEFAULT_BIG_INTERVAL);

		boolean isRunning = settings.getBoolean(
				Common.PREF_IS_SERVICE_RUNNING,
				true);

		int dataInterval = settings.getInt(Common.PREF_DATA_INTERVAL,
				Common.DEFAULT_DATA_INTERVAL);

		mSurveyUrl = settings.getString(Common.PREF_FIRST_SURVEY, null);

		String token = settings.getString(Common.PREF_TOKEN, null);

		mSurveyUrl += token;

		boolean firstTime = settings.getBoolean(Common.PREF_FIRST_TIME, true);

		if (mSurveyUrl != null && firstTime) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Common.PREF_FIRST_TIME, false);
			editor.commit();
			launchDemoSurvey();
		}

		if (isRunning) {
			AnnoyingApplication.startService(this, bigInterval);
		}

		AnnoyingApplication.launchStatusIcon(this);

		AnnoyingApplication.startDataService(this, dataInterval);
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
		super.onDestroy();
		BugSenseHandler.closeSession(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_main_demo_survey) {
			launchDemoSurvey();
		} else if (v.getId() == R.id.activity_main_export_db) {
			ExportDatabaseFileTask task = new ExportDatabaseFileTask(this);
			task.execute();
		}
	}

	private void launchDemoSurvey() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(mSurveyUrl));
		startActivity(i);
	}
}
