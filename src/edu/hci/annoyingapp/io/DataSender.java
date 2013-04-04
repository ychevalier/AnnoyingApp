package edu.hci.annoyingapp.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.model.XMLNode;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;

public class DataSender extends AsyncTask<Void, Void, Void> {

	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	private static final String TAG = DataSender.class.getSimpleName();

	private static final String TAG_DIALOGS = "dialogs";
	private static final String ATT_UID = "uid";

	private static final String TAG_DIALOG = "dialog";
	private static final String ATT_START = "start";
	private static final String ATT_CONDITION = "condition";

	private static final String TAG_INTERACTION = "interaction";
	private static final String ATT_BUTTON = "button";
	private static final String ATT_DATETIME = "datetime";

	private static final String FAKE_CONTENT = "<?xml version='1.0' encoding='utf-8'?>"
			+ "<dialogs uid='42'>"
			+ "<dialog start='123456789' condition='1'>"
			+ "	<interaction button='1' datetime='0987654321'/>"
			+ "	<interaction button='2' datetime='0979865451'/>"
			+ "	<interaction button='1' datetime='0987798756'/>"
			+ "</dialog>"
			+ "<dialog start='123457644' condition='1'>"
			+ "	<interaction button='1' datetime='0987654321'/>"
			+ "	<interaction button='2' datetime='0979865451'/>"
			+ "	<interaction button='1' datetime='0987798756'/>"
			+ "</dialog>"
			+ "<dialog start='123975469' condition='2'>"
			+ "	<interaction button='1' datetime='0980987991'/>"
			+ "	<interaction button='2' datetime='0977767671'/>"
			+ "	<interaction button='1' datetime='0910980986'/>"
			+ "</dialog>"
			+ "</dialogs>";

	private Context mContext;

	public DataSender(Context context) {
		mContext = context;
	}

	protected Void doInBackground(Void... args) {
		// postData(FAKE_CONTENT);

		XMLNode root = new XMLNode(TAG_DIALOGS);
		Cursor dialogs = mContext.getContentResolver().query(
				Dialogs.CONTENT_URI, null, null, null, null);

		while (dialogs.moveToNext()) {
			XMLNode dialog = new XMLNode(TAG_DIALOG);
			root.addChild(dialog);

			root.addAttribute(ATT_CONDITION, String.valueOf(dialogs
					.getInt(dialogs.getColumnIndex(Dialogs.DIALOG_CONDITION))));
			root.addAttribute(ATT_START, String.valueOf(dialogs.getLong(dialogs
					.getColumnIndex(Dialogs.DIALOG_START))));
			root.addAttribute(ATT_UID, String.valueOf(dialogs.getInt(dialogs
					.getColumnIndex(Dialogs.DIALOG_UID))));

			Cursor interactions = mContext
					.getContentResolver()
					.query(Dialogs
							.buildDialogsUri(String.valueOf(dialogs
									.getInt(dialogs
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
		}

		//postData(root.toString());
		if(DEBUG_MODE) {
			Log.d(TAG, root.toString());
		}
		return null;
	}

	private void postData(String content) {
		HttpPost httpPost = new HttpPost(AnnoyingApplication.WS_Server);
		httpPost.addHeader("Content-Type", "application/xml");

		StringEntity entity;
		try {

			entity = new StringEntity(content, "UTF-8");

			entity.setContentType("application/xml");
			httpPost.setEntity(entity);

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = (HttpResponse) httpclient
					.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				if (DEBUG_MODE) {
					Log.d(TAG, strResult);
				}

			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
