package edu.hci.annoyingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;
import edu.hci.annoyingapp.AnnoyingApplication;

public class SurveyActivity extends FragmentActivity {
	
	private static final String TAG = SurveyActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String EXTRA_SURVEY = "edu.hci.annoyingapp.activities.SurveyActivity.EXTRA_SURVEY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WebView webview = new WebView(this);
		setContentView(webview);

		String url = getIntent().getStringExtra(EXTRA_SURVEY);
		
		if(DEBUG_MODE) {
			Log.d(TAG, "This is a new survey : " + url);
		}
		
		webview.loadUrl(url);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}
