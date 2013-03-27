package edu.hci.annoyingapp.dialogs;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AnnoyingDialog extends DialogFragment implements OnClickListener {

	public static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	public static final String CONFIG_TYPE = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.configtype";

	public interface AnnoyingListener {
		public void onPositiveButtonClicked();

		public void onNegativeButtonClicked();
	}

	private AnnoyingListener mListener;

	private int mType;

	public static AnnoyingDialog newInstance(Bundle extras) {
		AnnoyingDialog f = new AnnoyingDialog();
		f.setArguments(extras);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mType = getArguments().getInt(AnnoyingDialog.CONFIG_TYPE,
				AnnoyingActivity.CONFIG_1);

		View v = null;
		Button yes = null;
		Button no = null;

		switch (mType) {
		case AnnoyingActivity.CONFIG_1:
			v = inflater.inflate(R.layout.dialog_annoying, container, false);
			// Watch for button clicks.
			yes = (Button) v.findViewById(R.id.activity_annoying_yes_button);
			no = (Button) v.findViewById(R.id.activity_annoying_no_button);
			break;
		case AnnoyingActivity.CONFIG_2:
			v = inflater.inflate(R.layout.dialog_annoying_2, container, false);
			// Watch for button clicks.
			yes = (Button) v.findViewById(R.id.activity_annoying_yes_button_2);
			no = (Button) v.findViewById(R.id.activity_annoying_no_button_2);
			break;
		}

		yes.setOnClickListener(this);
		no.setOnClickListener(this);

		return v;
	}

	public void setAnnoyingListener(AnnoyingListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.activity_annoying_yes_button 
				|| v.getId() == R.id.activity_annoying_yes_button_2) {
			if (mListener != null) {
				mListener.onPositiveButtonClicked();
			}
		} else if (v.getId() == R.id.activity_annoying_no_button 
				|| v.getId() == R.id.activity_annoying_no_button_2) {
			if (mListener != null) {
				mListener.onNegativeButtonClicked();
			}
		}
	}

}
