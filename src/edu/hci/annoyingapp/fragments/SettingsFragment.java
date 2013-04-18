package edu.hci.annoyingapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.utils.Common;

public class SettingsFragment extends Fragment implements OnClickListener {

	public static final String TAG = SettingsFragment.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String CONDITION = "edu.hci.annoyingapp.fragments.SettingsFragment.condition";
	public static final String BIG_INTERVAL = "edu.hci.annoyingapp.fragments.SettingsFragment.biginterval";
	public static final String LITTLE_INTERVAL = "edu.hci.annoyingapp.fragments.SettingsFragment.littleinterval";
	public static final String IS_RUNNING = "edu.hci.annoyingapp.fragments.SettingsFragment.isrunning";

	public interface OnSettingChoiceListener {
		public void onViewStats();
		public void onUnregister();
	}
	
	private Button mStartBt;
	private Button mStopBt;
	
	private int mBigInterval;
	private int mLittleInterval;
	private int mCondition;
	private boolean mIsRunning;

	public static SettingsFragment newInstance(Bundle args) {
		SettingsFragment f = new SettingsFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCondition = getArguments().getInt(SettingsFragment.CONDITION,
				Common.DEFAULT_CONDITION);
		
		mBigInterval  = getArguments().getInt(SettingsFragment.BIG_INTERVAL,
				Common.DEFAULT_BIG_INTERVAL);
		
		mLittleInterval  = getArguments().getInt(SettingsFragment.LITTLE_INTERVAL,
				Common.DEFAULT_LITTLE_INTERVAL);
		
		mIsRunning = getArguments().getBoolean(SettingsFragment.IS_RUNNING,
				Common.DEFAULT_IS_RUNNING);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);

		TextView running = (TextView) view
				.findViewById(R.id.fragment_settings_run);
		
		if(mIsRunning) {
			running.setText("Service is running.");
		} else {
			running.setText("Service is not running.");
		}

		Button stats = (Button) view
				.findViewById(R.id.fragment_settings_view_stats);
		stats.setOnClickListener(this);
		
		Button unreg = (Button) view
				.findViewById(R.id.fragment_settings_unregister);
		unreg.setOnClickListener(this);

		TextView time = (TextView) view
				.findViewById(R.id.fragment_settings_interval);
		time.setText(String.format(getString(R.string.time), mBigInterval, mLittleInterval));
		
		TextView condition = (TextView) view
				.findViewById(R.id.fragment_settings_condition);
		condition.setText("Condition is " + mCondition);
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Crash if Activity does not implement OnSettingChoiceListener :)
		@SuppressWarnings("unused")
		OnSettingChoiceListener l = (OnSettingChoiceListener) activity;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.fragment_settings_view_stats) {
			if (getActivity() != null) {
				((OnSettingChoiceListener) getActivity()).onViewStats();
			}
		} else if(v.getId() == R.id.fragment_settings_unregister) {
			if (getActivity() != null) {
				((OnSettingChoiceListener) getActivity()).onUnregister();
			}
		}
	}
}
