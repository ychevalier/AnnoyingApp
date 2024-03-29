package edu.hci.annoyingapp.io;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.model.XMLNode;
import edu.hci.annoyingapp.network.ServerUtilities;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;

public class DataSender {

	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	private static final String TAG = DataSender.class.getSimpleName();

	private static final String TAG_DIALOGS = "dialogs";
	private static final String ATT_UID = "uid";

	private static final String TAG_DIALOG = "dialog";
	private static final String ATT_START = "start";
	private static final String ATT_CONDITION = "condition";
	private static final String ATT_TITLE = "title";
	private static final String ATT_TEXT = "text";
	private static final String ATT_THEME = "theme";
	private static final String ATT_POSITION = "position";
	private static final String ATT_IMAGE = "image";
	private static final String ATT_TOP_IMAGE = "top_image";
	private static final String ATT_BOTTOM_IMAGE = "bottom_image";
	
	private static final String TAG_INTERACTION = "interaction";
	private static final String ATT_BUTTON = "button";
	private static final String ATT_DATETIME = "datetime";

	public static boolean sendData(Context context, long last, String uid) {
		if(context == null) return false;

		XMLNode root = new XMLNode(TAG_DIALOGS);
		Cursor dialogs = context.getContentResolver().query(
				Dialogs.buildDialogsLastUri(last), Dialogs.DialogsLastQuery.PROJECTION, null, null, null);

		while (dialogs.moveToNext()) {
			XMLNode dialog = new XMLNode(TAG_DIALOG);
			root.addChild(dialog);

			dialog.addAttribute(ATT_CONDITION, String.valueOf(dialogs
					.getInt(Dialogs.DialogsLastQuery.CONDITION)));
			dialog.addAttribute(ATT_START, String.valueOf(dialogs
					.getLong(Dialogs.DialogsLastQuery.START)));
			dialog.addAttribute(ATT_TITLE, dialogs
					.getString(Dialogs.DialogsLastQuery.TITLE));
			dialog.addAttribute(ATT_TEXT, dialogs
					.getString(Dialogs.DialogsLastQuery.TEXT));
			dialog.addAttribute(ATT_THEME, dialogs
					.getString(Dialogs.DialogsLastQuery.THEME));
			dialog.addAttribute(ATT_POSITION, dialogs
					.getString(Dialogs.DialogsLastQuery.POSITION));
			dialog.addAttribute(ATT_IMAGE, dialogs
					.getString(Dialogs.DialogsLastQuery.IMAGE));
			dialog.addAttribute(ATT_TOP_IMAGE, dialogs
					.getString(Dialogs.DialogsLastQuery.TOP_IMAGE));
			dialog.addAttribute(ATT_BOTTOM_IMAGE, dialogs
					.getString(Dialogs.DialogsLastQuery.BOTTOM_IMAGE));
			root.addAttribute(ATT_UID, uid);

			Cursor interactions = context.getContentResolver()
					.query(Dialogs.buildInteractionDirUri(String
							.valueOf(dialogs.getInt(dialogs
									.getColumnIndex(Dialogs.DIALOG_ID)))),
							null, null, null, null);

			while (interactions.moveToNext()) {
				XMLNode interaction = new XMLNode(TAG_INTERACTION);

				dialog.addChild(interaction);

				interaction
						.addAttribute(
								ATT_BUTTON,
								String.valueOf(interactions.getInt(interactions
										.getColumnIndex(Interactions.INTERACTION_BUTTON))));
				interaction
						.addAttribute(
								ATT_DATETIME,
								String.valueOf(interactions.getLong(interactions
										.getColumnIndex(Interactions.INTERACTION_DATETIME))));

			}

			interactions.close();
		}

		dialogs.close();

		if (DEBUG_MODE) {
			Log.d(TAG, root.toString());
		}
		
		if(root.isEmpty()) {
			return false;
		} else {
			return ServerUtilities.postXMLData(root.toString());
		}
	}

	
}
