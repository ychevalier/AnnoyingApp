package edu.hci.annoyingapp.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.AnnoyingActivity;

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

	private Button mPositive;
	private Button mNegative;
	
	public static AnnoyingDialog newInstance(Bundle extras) {
		AnnoyingDialog f = new AnnoyingDialog();
		f.setArguments(extras);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(R.string.dialog_title);
		
		mType = getArguments().getInt(AnnoyingDialog.CONFIG_TYPE,
				AnnoyingApplication.CONFIG_DEFAULT);
		
		View v = null;
		
		// We use Simulated Holo theme after honeycomb, normal theme after.
		// Also, before honeycomb default is YES/NO and after it is NO/YES
		
		if(mType == AnnoyingApplication.CONFIG_DEFAULT) {
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
				v = inflater.inflate(R.layout.dialog_annoying_holo, container, false);
				mPositive = (Button) v.findViewById(R.id.activity_annoying_right_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_left_button);
			} else {
				v = inflater.inflate(R.layout.dialog_annoying, container, false);
				mPositive = (Button) v.findViewById(R.id.activity_annoying_left_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_right_button);
			}
		} else if(mType == AnnoyingApplication.CONFIG_ALT) {
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
				v = inflater.inflate(R.layout.dialog_annoying_holo, container, false);
				mPositive = (Button) v.findViewById(R.id.activity_annoying_left_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_right_button);
			} else {
				v = inflater.inflate(R.layout.dialog_annoying, container, false);
				mPositive = (Button) v.findViewById(R.id.activity_annoying_right_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_left_button);
			}
		} else if(mType == AnnoyingApplication.CONFIG_OTHER) {
			v = inflater.inflate(R.layout.dialog_annoying_other, container, false);
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
				mPositive = (Button) v.findViewById(R.id.activity_annoying_right_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_left_button);
			} else {
				mPositive = (Button) v.findViewById(R.id.activity_annoying_left_button);
				mNegative = (Button) v.findViewById(R.id.activity_annoying_right_button);
			}
		}

		mPositive.setText(android.R.string.ok);
		mNegative.setText(android.R.string.cancel);
		
		mPositive.setOnClickListener(this);
		mNegative.setOnClickListener(this);
		return v;
	}
	
	public void setAnnoyingListener(AnnoyingListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v == mPositive) {
			if (mListener != null) {
				mListener.onPositiveButtonClicked();
			}
		} else if (v == mNegative) {
			if (mListener != null) {
				mListener.onNegativeButtonClicked();
			}
		}
	}
}
