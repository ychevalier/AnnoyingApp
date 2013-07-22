package edu.hci.annoyingapp.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.io.GlobalRegistration;
import edu.hci.annoyingapp.io.GlobalRegistration.OnRegistrationOverListener;
import edu.hci.annoyingapp.protocol.Receivers;

/**
 * Login UI for the Demo App.
 * You enter your email address and start the 3step registration.
 * Once completed, this activity calls the main activity.
 */
public class LoginActivity extends Activity implements
		OnRegistrationOverListener, OnClickListener {

	public static final boolean DEBUG_MODE = true;

	private GlobalRegistration mRegistration;

	private EditText mEmail;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity);

		mEmail = (EditText) findViewById(R.id.email_et);

		Button bt = (Button) findViewById(R.id.email_bt);
		bt.setOnClickListener(this);

		registerReceiver(mHandleRegistrationReceiver, new IntentFilter(
				Receivers.REGISTERED));
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
			startMainActivity();
			//Toast.makeText(this, R.string.already_connected, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegistration != null) {
			mRegistration.onDestroy();
		}
		unregisterReceiver(mHandleRegistrationReceiver);
		GCMRegistrar.onDestroy(getApplicationContext());
		super.onDestroy();
	}

	@Override
	public void onRegistrationSuccess() {
		startMainActivity();
		mDialog.dismiss();
		Toast.makeText(this, R.string.connection_success, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onRegistrationFailure() {
		// mDisplay.setText("Registration failed.");
		mDialog.dismiss();
		Toast.makeText(this, R.string.connection_failure, Toast.LENGTH_SHORT).show();
	}

	private final BroadcastReceiver mHandleRegistrationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			boolean success = intent.getExtras().getBoolean(Receivers.REGISTERED_SUCCESS,
					false);
			if (success) {
				onRegistrationSuccess();
			} else {
				onRegistrationFailure();
			}
		}
	};

	public void startMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.email_bt) {

			if (TextUtils.isEmpty(mEmail.getText())) {
				mEmail.setError(getString(R.string.mail_error));
				return;
			}

			mRegistration = new GlobalRegistration(getApplicationContext(), this);
			mRegistration.register(mEmail.getText().toString());

			mDialog = ProgressDialog.show(this, "", getString(R.string.dialog_connecting), true);
		}
	}

}