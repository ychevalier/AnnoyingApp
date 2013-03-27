package edu.hci.annoyingapp.adapters;

import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import edu.hci.annoyingapp.model.Stat;

public class StatsAdapter extends ArrayAdapter<Stat> {

	public static final String TAG = StatsAdapter.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	static class StatHolder {
		TextView time;
		TextView duration;
		TextView failure;
		TextView config;
		TextView hasquit;
	}

	Context mContext;
	int mLayoutId;
	Stat[] mStats;

	public StatsAdapter(Context context, int layoutId, Stat[] stats) {
		super(context, layoutId, stats);
		mContext = context;
		mLayoutId = layoutId;
		mStats = stats;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StatHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutId, parent, false);

			holder = new StatHolder();
			holder.time = (TextView) row.findViewById(R.id.row_data_time);
			holder.duration = (TextView) row
					.findViewById(R.id.row_data_duration);
			holder.failure = (TextView) row.findViewById(R.id.row_data_failure);
			holder.config = (TextView) row.findViewById(R.id.row_data_config);
			holder.hasquit = (TextView) row
					.findViewById(R.id.row_data_has_quit);
			row.setTag(holder);
		} else {
			holder = (StatHolder) row.getTag();
		}

		Stat data = mStats[position];

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getStartTime());

		StringBuilder sB = new StringBuilder();
		sB.append(cal.get(Calendar.HOUR_OF_DAY));
		sB.append(':');
		sB.append(cal.get(Calendar.MINUTE));
		sB.append(':');
		sB.append(cal.get(Calendar.SECOND));

		holder.time.setText(sB.toString());
		holder.duration.setText(String.valueOf(data.getStopTime()
				- data.getStartTime()));
		holder.failure.setText(String.valueOf(data.getNbFailures()));

		switch (data.getConfig()) {
		case AnnoyingActivity.CONFIG_1:
			holder.config.setText(getContext().getResources().getString(
					R.string.config_1));
			break;
		case AnnoyingActivity.CONFIG_2:
			holder.config.setText(getContext().getResources().getString(
					R.string.config_2));
			break;
		}

		holder.hasquit.setText(data.getHasQuitProperly() ? getContext()
				.getResources().getString(R.string.has_quit_properly)
				: getContext().getResources().getString(
						R.string.has_not_quit_properly));

		return row;
	}
}
