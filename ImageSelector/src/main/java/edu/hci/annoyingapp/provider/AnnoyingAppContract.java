package edu.hci.annoyingapp.provider;

import edu.hci.annoyingapp.provider.AnnoyingDatabase.Tables;
import android.net.Uri;
import android.provider.BaseColumns;

public class AnnoyingAppContract {

	public static final String CONTENT_AUTHORITY = "edu.hci.annoyingapp";

	private static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);

	public static final String PATH_DIALOGS = "dialogs";
	public static final String PATH_INTERACTIONS = "interactions";

	interface DialogsColumns {
		String DIALOG_ID = "_id";
		String DIALOG_START = "start";
		String DIALOG_THEME = "theme";
		String DIALOG_CONDITION = "condition";
		String DIALOG_TOP_IMAGE = "top_image";
		String DIALOG_BOTTOM_IMAGE = "bottom_image";
		String DIALOG_IMAGE = "image";
		String DIALOG_POSITION = "position";
		String DIALOG_TEXT = "text";
		String DIALOG_TITLE = "title";
	}

	interface InteractionsColumns {
		String INTERACTION_ID = "_id";
		String INTERACTION_BUTTON = "button";
		String INTERACTION_DATETIME = "datetime";
		String INTERACTION_DIALOG_ID = "dialog_id";
	}

	public static class Dialogs implements DialogsColumns, BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_DIALOGS).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.android.annoyingapp.dialog";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.android.annoyingapp.dialog";

		// Get One dialog.
		public static Uri buildDialogsUri(String dialogId) {
			return CONTENT_URI.buildUpon().appendPath(dialogId).build();
		}

		// Get all interactions for one dialog.
		public static Uri buildInteractionDirUri(String dialogId) {
			return CONTENT_URI.buildUpon().appendPath(dialogId)
					.appendPath(PATH_INTERACTIONS).build();
		}
		
		// Get all dialogs in a special layout
		public static Uri buildDialogsLastUri(long time) {
			return CONTENT_URI.buildUpon()
					.appendPath(AnnoyingAppProvider.LAST).appendPath(String.valueOf(time)).build();
		}

		public interface DialogsLastQuery {
			String[] PROJECTION = {
					Tables.DIALOGS + '.' + Dialogs._ID,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_THEME,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_CONDITION,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_START,
					"MAX(" + Tables.INTERACTIONS + '.'
							+ Interactions.INTERACTION_DATETIME + ")",
					Tables.DIALOGS + '.' + Dialogs.DIALOG_TOP_IMAGE,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_BOTTOM_IMAGE,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_IMAGE,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_POSITION,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_TEXT,
					Tables.DIALOGS + '.' + Dialogs.DIALOG_TITLE};

			int _ID = 0;
			int THEME = 1;
			int CONDITION = 2;
			int START = 3;
			int STOP = 4;
			int TOP_IMAGE = 5;
			int BOTTOM_IMAGE = 6;
			int IMAGE = 7;
			int POSITION = 8;
			int TEXT = 9;
			int TITLE = 10;
		}
		
		public static String getDialogIdFromDialogInteractions(Uri uri) {
		    return uri.getPathSegments().get(1);
		}
	}

	public static class Interactions implements InteractionsColumns,
			BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_INTERACTIONS).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.android.annoyingapp.interaction";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.android.annoyingapp.interaction";

		// Get one interaction.
		public static Uri buildInteractionsUri(String interactionId) {
			return CONTENT_URI.buildUpon().appendPath(interactionId).build();
		}
	}

}
