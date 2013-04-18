package edu.hci.annoyingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import edu.hci.annoyingapp.protocol.PushMessages;

public class SurveyActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView webview = new WebView(this);
		setContentView(webview);
		
		String url = getIntent().getStringExtra(PushMessages.EXTRA_SURVEY);
		webview.loadUrl(url);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}
