package edu.hci.annoyingapp.ui.views;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import edu.hci.annoyingapp.R;

public class CustomImageButton extends ImageButton implements OnTouchListener {

	public CustomImageButton(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	public CustomImageButton(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	public CustomImageButton(final Context context) {
		super(context);
		this.init();
	}

	private void init() {
		super.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(final View view, final MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// touchDown event -> Change color overlay. goes here
				this.setColorFilter(getResources().getColor(R.color.transparent_blue), Mode.MULTIPLY);
				break;

			case MotionEvent.ACTION_UP:
				// touchUp event -> remove color overlay, and perform button action.
				this.setColorFilter(getResources().getColor(R.color.transparent), Mode.MULTIPLY);
				break;
		}
		return super.onTouchEvent(event);
	}
}