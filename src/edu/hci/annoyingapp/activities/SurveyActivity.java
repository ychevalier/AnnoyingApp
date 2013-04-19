package edu.hci.annoyingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

public class SurveyActivity extends FragmentActivity {
	
	public static final String EXTRA_SURVEY = "edu.hci.annoyingapp.activities.SurveyActivity.EXTRA_SURVEY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView webview = new WebView(this);
		setContentView(webview);
		
		String url = getIntent().getStringExtra(EXTRA_SURVEY);
		webview.loadUrl(url);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}
