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

	public interface AnnoyingListener {
		public void onPositiveButtonClicked();
		public void onNegativeButtonClicked();
	}
	
	private AnnoyingListener mListener;

	public static AnnoyingDialog newInstance() {
		AnnoyingDialog f = new AnnoyingDialog();

		// Supply num input as an argument.
		//Bundle args = new Bundle();
		//args.put...
		//f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_annoying, container, false);

		// Watch for button clicks.
		Button yes = (Button) v
				.findViewById(R.id.activity_annoying_yes_button);

		yes.setOnClickListener(this);
		
		Button no = (Button) v
				.findViewById(R.id.activity_annoying_no_button);

		no.setOnClickListener(this);

		return v;
	}
	
	public void setAnnoyingListener(AnnoyingListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.activity_annoying_yes_button) {
			if(mListener != null) {
				mListener.onPositiveButtonClicked();
			}
		} else if(v.getId() == R.id.activity_annoying_no_button) {
			if(mListener != null) {
				mListener.onNegativeButtonClicked();
			}
		}
	}
}
