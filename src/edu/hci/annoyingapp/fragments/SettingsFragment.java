package edu.hci.annoyingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;

public class SettingsFragment extends Fragment implements OnClickListener {
	
	public static final String TAG = SettingsFragment.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	public interface OnSettingChoiceListener {
		public void onServiceStart();
		public void onServiceStop();
		public void onViewStats();
		public void onSetTime();
	}
	
	private OnSettingChoiceListener mListener;
	
	public static SettingsFragment newInstance() {
		SettingsFragment f = new SettingsFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings,
				container, false);

		Button start = (Button) view
				.findViewById(R.id.fragment_settings_start_button);
		start.setOnClickListener(this);
		
		Button stop = (Button) view
				.findViewById(R.id.fragment_settings_stop_button);
		stop.setOnClickListener(this);
		
		Button stats = (Button) view
				.findViewById(R.id.fragment_settings_view_stats);
		stats.setOnClickListener(this);
		
		Button time = (Button) view
				.findViewById(R.id.fragment_settings_set_interval);
		time.setOnClickListener(this);

		return view;
	}
	
	
	public void setOnSettingChoiceListener(OnSettingChoiceListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.fragment_settings_start_button) {
			if(mListener != null) {
				mListener.onServiceStart();
			}
			
		} else if (v.getId() == R.id.fragment_settings_stop_button) {
			if(mListener != null) {
				mListener.onServiceStop();
			}
		} else if (v.getId() == R.id.fragment_settings_set_interval) {
			if(mListener != null) {
				mListener.onSetTime();
			}
		} else if (v.getId() == R.id.fragment_settings_view_stats) {
			if(mListener != null) {
				mListener.onViewStats();
			}
		}
	}

}
