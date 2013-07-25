package edu.hci.annoyingapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;

import com.bugsense.trace.BugSenseHandler;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.utils.Common;

public class SurveyActivity extends FragmentActivity {

	private static final String TAG = SurveyActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String EXTRA_SURVEY = "edu.hci.annoyingapp.ui.activities.SurveyActivity.EXTRA_SURVEY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(this, Common.BUGSENSE_KEY);

		WebView webview = new WebView(this);
		setContentView(webview);

		String url = getIntent().getStringExtra(EXTRA_SURVEY);

		if (DEBUG_MODE) {
			Log.d(TAG, "This is a new survey : " + url);
		}

		webview.loadUrl(url);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BugSenseHandler.closeSession(this);
	}
}
