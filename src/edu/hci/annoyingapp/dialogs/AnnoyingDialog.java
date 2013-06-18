package edu.hci.annoyingapp.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingDialog extends DialogFragment implements OnClickListener {

	public static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final String THEME = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.theme";
	public static final String TOP_IMAGE = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.top_image";
	public static final String BOTTOM_IMAGE = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.bottom_image";
	public static final String DIALOG_TEXT = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.dialog_text";
	public static final String DIALOG_TITLE = "edu.hci.annoyingapp.dialogs.AnnoyingDialog.dialog_title";

	public interface AnnoyingListener {
		public void onTopButtonClicked();

		public void onBottomButtonClicked();
	}

	private AnnoyingListener mListener;

	private String mTopImage;
	private String mBottomImage;
	private String mDialogText;
	private String mDialogTitle;

	private ImageButton mTopBt;
	private ImageButton mBottomBt;

	public static AnnoyingDialog newInstance(Bundle extras) {
		AnnoyingDialog f = new AnnoyingDialog();
		f.setArguments(extras);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTopImage = getArguments().getString(AnnoyingDialog.TOP_IMAGE);

		mBottomImage = getArguments().getString(AnnoyingDialog.BOTTOM_IMAGE);

		mDialogText = getArguments().getString(AnnoyingDialog.DIALOG_TEXT);

		mDialogTitle = getArguments().getString(AnnoyingDialog.DIALOG_TITLE);

		int theme = getArguments().getInt(AnnoyingDialog.THEME);

		switch (theme) {
		case Common.THEME_DARK:
			setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Dialog);
			break;
		case Common.THEME_LIGHT:
			setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		int topId = getResources().getIdentifier(mTopImage, "drawable",
				getActivity().getBaseContext().getPackageName());
		if (topId == 0) {
			return null;
		}

		int bottomId = getResources().getIdentifier(mBottomImage, "drawable",
				getActivity().getBaseContext().getPackageName());
		if (bottomId == 0) {
			return null;
		}

		getDialog().setTitle(mDialogTitle);

		View v = inflater.inflate(R.layout.dialog_annoying_holo, container,
				false);

		TextView tv = (TextView) v
				.findViewById(R.id.activity_annoying_content_text);
		tv.setText(mDialogText);

		mTopBt = (ImageButton) v.findViewById(R.id.activity_annoying_top_button);
		mBottomBt = (ImageButton) v
				.findViewById(R.id.activity_annoying_bottom_button);

		mTopBt.setImageDrawable(getActivity().getResources().getDrawable(topId));

		mBottomBt.setImageDrawable(getActivity().getResources().getDrawable(
				bottomId));

		mTopBt.setOnClickListener(this);
		mBottomBt.setOnClickListener(this);
		return v;
	}

	public void setAnnoyingListener(AnnoyingListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v == mTopBt) {
			if (mListener != null) {
				mListener.onTopButtonClicked();
			}
		} else if (v == mBottomBt) {
			if (mListener != null) {
				mListener.onBottomButtonClicked();
			}
		}
	}
}
