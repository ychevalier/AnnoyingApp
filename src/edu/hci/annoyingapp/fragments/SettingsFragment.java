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

public class SettingsFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener {

	public static final String TAG = SettingsFragment.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String CONFIG_TYPE = "edu.hci.annoyingapp.fragments.SettingsFragment.configtype";
	public static final String BIG_INTERVAL = "edu.hci.annoyingapp.fragments.SettingsFragment.biginterval";
	public static final String LITTLE_INTERVAL = "edu.hci.annoyingapp.fragments.SettingsFragment.littleinterval";
	public static final String IS_RUNNING = "edu.hci.annoyingapp.fragments.SettingsFragment.isrunning";

	public interface OnSettingChoiceListener {
		public void onServiceStart();

		public void onServiceStop();

		public void onViewStats();

		public void onSetBigInterval(int bigInterval);
		
		public void onSetLittleInterval(int littleInterval);

		public void onConfigChanged(int config);
	}
	
	private Button mStartBt;
	private Button mStopBt;
	
	private int mBigInterval;
	private int mLittleInterval;
	private int mConfig;
	private boolean mIsRunning;

	public static SettingsFragment newInstance(Bundle args) {
		SettingsFragment f = new SettingsFragment();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mConfig = getArguments().getInt(SettingsFragment.CONFIG_TYPE,
				Common.DEFAULT_CONFIG);
		
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

		mStartBt = (Button) view
				.findViewById(R.id.fragment_settings_start_button);
		mStartBt.setOnClickListener(this);

		mStopBt = (Button) view
				.findViewById(R.id.fragment_settings_stop_button);
		mStopBt.setOnClickListener(this);
		
		if(mIsRunning) {
			mStartBt.setEnabled(false);
			mStopBt.setEnabled(true);
		} else {
			mStartBt.setEnabled(true);
			mStopBt.setEnabled(false);
		}

		Button stats = (Button) view
				.findViewById(R.id.fragment_settings_view_stats);
		stats.setOnClickListener(this);

		TextView time = (TextView) view
				.findViewById(R.id.fragment_settings_set_interval);
		time.setText(String.format(getString(R.string.time), mBigInterval, mLittleInterval));
		
		RadioGroup config = (RadioGroup) view
				.findViewById(R.id.fragment_settings_config);
		
		switch (mConfig) {
		case Common.CONFIG_DEFAULT:
			config.check(R.id.fragment_settings_config_1);
			break;
		case Common.CONFIG_ALT:
			config.check(R.id.fragment_settings_config_2);
			break;
		case Common.CONFIG_OTHER:
			config.check(R.id.fragment_settings_config_3);
			break;
		}
		config.setOnCheckedChangeListener(this);

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
		if (v.getId() == R.id.fragment_settings_start_button) {
			if (getActivity() != null) {
				mStartBt.setEnabled(false);
				mStopBt.setEnabled(true);
				mIsRunning = true;
				((OnSettingChoiceListener) getActivity()).onServiceStart();
			}

		} else if (v.getId() == R.id.fragment_settings_stop_button) {
			if (getActivity() != null) {
				mStartBt.setEnabled(true);
				mStopBt.setEnabled(false);
				mIsRunning = false;
				((OnSettingChoiceListener) getActivity()).onServiceStop();
			}
		} else if (v.getId() == R.id.fragment_settings_view_stats) {
			if (getActivity() != null) {
				((OnSettingChoiceListener) getActivity()).onViewStats();
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.fragment_settings_config_1:
			if (getActivity() != null) {
				mConfig = Common.CONFIG_DEFAULT;
				((OnSettingChoiceListener) getActivity()).onConfigChanged(Common.CONFIG_DEFAULT);
			}
			break;
		case R.id.fragment_settings_config_2:
			if (getActivity() != null) {
				mConfig = Common.CONFIG_ALT;
				((OnSettingChoiceListener) getActivity()).onConfigChanged(Common.CONFIG_ALT);
			}
			break;
		case R.id.fragment_settings_config_3:
			if (getActivity() != null) {
				mConfig = Common.CONFIG_OTHER;
				((OnSettingChoiceListener) getActivity()).onConfigChanged(Common.CONFIG_OTHER);
			}
			break;
		}
	}

}
