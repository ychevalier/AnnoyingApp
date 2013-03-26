package edu.hci.annoyingapp.fragments;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.adapters.StatsAdapter;
import android.support.v4.app.ListFragment;

public class StatsFragment extends ListFragment {
	
	public static final String TAG = StatsFragment.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static StatsFragment newInstance() {
		StatsFragment f = new StatsFragment();
		return f;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		StatsAdapter adapter = new StatsAdapter(getActivity(), R.layout.row_stat, AnnoyingApplication.getStats());
		setListAdapter(adapter);
	}

}
