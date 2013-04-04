package edu.hci.annoyingapp.fragments;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.adapters.StatsAdapter;
import edu.hci.annoyingapp.io.DataSender;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public class StatsFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	public static final String TAG = StatsFragment.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	private StatsAdapter mAdapter;

	public static StatsFragment newInstance() {
		StatsFragment f = new StatsFragment();
		return f;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		getLoaderManager().restartLoader(0, null, this);
		if (mAdapter == null) {
			mAdapter = new StatsAdapter(getActivity());
			setListAdapter(mAdapter);
		}
		
		new DataSender(getActivity()).execute();
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				Dialogs.buildDialogsSpecialUri(), null, null, null, null);
		return cursorLoader;
	}

}
