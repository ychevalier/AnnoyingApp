package edu.hci.annoyingapp.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingDialog extends DialogFragment implements OnClickListener {

	public static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String CONDITION = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.condition";
	public static final String POSITIVE_TEXT = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.positive_text";
	public static final String NEGATIVE_TEXT = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.negative_text";
	public static final String DIALOG_TEXT = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.dialog_text";	
	public static final String DIALOG_TITLE = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.dialog_title";	
	
	public interface AnnoyingListener {
		public void onPositiveButtonClicked();

		public void onNegativeButtonClicked();
	}

	private AnnoyingListener mListener;

	private int mCondition;
	private String mPositiveText;
	private String mNegativeText;
	private String mDialogText;
	private String mDialogTitle;

	private Button mPositiveBt;
	private Button mNegativeBt;

	public static AnnoyingDialog newInstance(Bundle extras) {
		AnnoyingDialog f = new AnnoyingDialog();
		f.setArguments(extras);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCondition = getArguments().getInt(AnnoyingDialog.CONDITION,
				Common.CONDITION_DEFAULT);
		
		mPositiveText = getArguments().getString(AnnoyingDialog.POSITIVE_TEXT,
				Common.DEFAULT_POSITIVE);
		
		mNegativeText = getArguments().getString(AnnoyingDialog.NEGATIVE_TEXT,
				Common.DEFAULT_NEGATIVE);
		
		mDialogText = getArguments().getString(AnnoyingDialog.DIALOG_TEXT,
				Common.DEFAULT_DIALOG);
		
		mDialogTitle = getArguments().getString(AnnoyingDialog.DIALOG_TITLE,
				Common.DEFAULT_DIALOG_TITLE);

		if(mCondition == Common.CONDITION_OTHER) {
			setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Dialog);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(mDialogTitle);
		
		View v = inflater.inflate(R.layout.dialog_annoying_holo, container,
				false);
		
		TextView tv = (TextView) v.findViewById(R.id.activity_annoying_content_text);
		tv.setText(mDialogText);

		if (mCondition == Common.CONDITION_DEFAULT || mCondition == Common.CONDITION_OTHER ) {
			mPositiveBt = (Button) v
					.findViewById(R.id.activity_annoying_right_button);
			mNegativeBt = (Button) v
					.findViewById(R.id.activity_annoying_left_button);
		} else if (mCondition == Common.CONDITION_ALT) {
			mPositiveBt = (Button) v
					.findViewById(R.id.activity_annoying_left_button);
			mNegativeBt = (Button) v
					.findViewById(R.id.activity_annoying_right_button);
		}

		mPositiveBt.setText(mPositiveText);
		mNegativeBt.setText(mNegativeText);

		mPositiveBt.setOnClickListener(this);
		mNegativeBt.setOnClickListener(this);
		return v;
	}

	public void setAnnoyingListener(AnnoyingListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v == mPositiveBt) {
			if (mListener != null) {
				mListener.onPositiveButtonClicked();
			}
		} else if (v == mNegativeBt) {
			if (mListener != null) {
				mListener.onNegativeButtonClicked();
			}
		}
	}
}
