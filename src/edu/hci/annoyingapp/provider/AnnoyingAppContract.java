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
		// TODO : what about _ID?
		String DIALOG_ID = "_id";
		String DIALOG_START = "start";
		String DIALOG_CONDITION = "condition";
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
		public static Uri buildDialogsSpecialUri() {
			return CONTENT_URI.buildUpon().appendPath(AnnoyingAppProvider.SPECIAL).build();
		}
		
		public interface SpecialQuery {
			
			String[] PROJECTION = {Tables.DIALOGS +'.'+ Dialogs._ID, 
					Tables.DIALOGS +'.'+ Dialogs.DIALOG_CONDITION, 
					Tables.DIALOGS +'.'+ Dialogs.DIALOG_START,
					Tables.INTERACTIONS +'.'+ Interactions.INTERACTION_DATETIME,
					Tables.INTERACTIONS +'.'+ Interactions.INTERACTION_BUTTON,
					"Count(*)"};

			int _ID = 0;
			int CONDITION = 1;
			int START = 2;
			int STOP = 3;
			int BUTTON = 4;
			int NB_FAILURES = 5;
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
