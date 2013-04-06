package edu.hci.annoyingapp.adapters;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs.SpecialQuery;

public class StatsAdapter extends CursorAdapter {
	
	public static final String TAG = StatsAdapter.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	static class StatHolder {
		TextView time;
		TextView duration;
		TextView failure;
		TextView config;
		TextView hasquit;
	}

	public StatsAdapter(Context context) {
		super(context, null, 0);
	}

	@Override
	public long getItemId(int position) {
		return ((Cursor) getItem(position)).getLong(mCursor
				.getColumnIndex(Dialogs._ID));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.row_stat, parent, false);

		StatHolder holder = new StatHolder();
		holder.time = (TextView) v.findViewById(R.id.row_data_time);
		holder.duration = (TextView)v
				.findViewById(R.id.row_data_duration);
		holder.failure = (TextView) v.findViewById(R.id.row_data_failure);
		holder.config = (TextView) v.findViewById(R.id.row_data_config);
		holder.hasquit = (TextView)v
				.findViewById(R.id.row_data_has_quit);
		v.setTag(holder);

		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		StatHolder holder = (StatHolder) view.getTag();
		
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cursor.getLong(SpecialQuery.START));

		StringBuilder sB = new StringBuilder();
		sB.append(cal.get(Calendar.HOUR_OF_DAY));
		sB.append(':');
		sB.append(cal.get(Calendar.MINUTE));
		sB.append(':');
		sB.append(cal.get(Calendar.SECOND));

		holder.time.setText(sB.toString());
		holder.duration.setText(String.valueOf(cursor.getLong(SpecialQuery.STOP)
				- cursor.getLong(SpecialQuery.START)));
		
		holder.failure.setText(String.valueOf(cursor.getInt(SpecialQuery.NB_FAILURES)));

		switch (cursor.getInt(SpecialQuery.CONDITION)) {
		case AnnoyingApplication.CONFIG_DEFAULT:
			holder.config.setText(context.getResources().getString(
					R.string.config_default));
			break;
		case AnnoyingApplication.CONFIG_ALT:
			holder.config.setText(context.getResources().getString(
					R.string.config_alt));
			break;
		case AnnoyingApplication.CONFIG_OTHER:
			holder.config.setText(context.getResources().getString(
					R.string.config_other));
			break;
		}

		
		boolean hasQuitProperly = false;;
		if(cursor.getInt(SpecialQuery.BUTTON) == AnnoyingApplication.BUTTON_YES) {
			hasQuitProperly = true;
		}
		holder.hasquit.setText(hasQuitProperly ? context
				.getResources().getString(R.string.has_quit_properly)
				: context.getResources().getString(R.string.has_not_quit_properly));
				
	}

}
