package edu.hci.annoyingserver.servlets.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import edu.hci.annoyingserver.ApiKeyInitializer;
import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.html.Attributes;
import edu.hci.annoyingserver.protocol.PushMessages;

@SuppressWarnings("serial")
public class SendAllMessagesServlet extends BaseServlet {

	private static final int MULTICAST_SIZE = 1000;

	private Sender mSender;

	private boolean mRun;

	private int mCondition;

	private int mTheme;
	private int mPosition;
	private boolean mNewImage;
	private String mDialogText;
	private String mDialogTitle;

	private int mLittleInterval;
	private int mBigInterval;
	private int mDataInterval;

	private String mSurvey;

	private Message mMessage;

	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mSender = newSender(config);
	}

	/**
	 * Creates the {@link Sender} based on the servlet settings.
	 */
	protected Sender newSender(ServletConfig config) {
		String key = (String) config.getServletContext().getAttribute(
				ApiKeyInitializer.ATTRIBUTE_ACCESS_KEY);
		return new Sender(key);
	}

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String status;
		boolean defaultMod = false;
		
		byte[] data = null;
		
		Map<String, String> params = new TreeMap<String, String>();
		
		try {
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
	        for (FileItem item : items) {
	            if (item.isFormField()) {
	                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
	                String fieldname = item.getFieldName();
	                String fieldvalue = item.getString();
	                params.put(fieldname, fieldvalue);
	                //System.out.println("This is my field : " + fieldname + ", " + fieldvalue);
	            } else {
	                // Process form file field (input type="file").
	                //String fieldname = item.getFieldName();
	                //String filename = FilenameUtils.getName(item.getName());
	                data = item.get();
	                //System.out.println("This is my file : " + fieldname + ", " + filename);
	            }
	        }
	    } catch (FileUploadException e) {
	        throw new ServletException("Cannot parse multipart request.", e);
	    }
		
		List<Integer> uids = new LinkedList<Integer>();
		
		
		String defaultBox = params.get(Attributes.DEFAULT_BOX);
		//if (run != null && run.length >= 1) {
		if (defaultBox != null && defaultBox.equals("on")) {
			defaultMod = true;
		} else {
			//String uidCommas = req.getParameter(Attributes.UID_COMMAS);
			String uidCommas = new String(data);
			
			if (uidCommas != null) {
	
				if (uidCommas.equals(Attributes.DEFAULT)) {
					defaultMod = true;
				} else {
					
					String[] rawUids = null;
					if(uidCommas.contains(",")) {
						rawUids = uidCommas.split("\\" + ',');
					} else {
						rawUids = uidCommas.split("\\" + '\n');
					}
	
					for (int i = 0; i < rawUids.length; i++) {
						try {
							uids.add(Integer.parseInt(rawUids[i].trim()));
						} catch (NumberFormatException e) {
							// Alright...
						}
					}
				}
			}
		}
		
		List<String> devices = null;
		if (!uids.isEmpty()) {
			devices = MySQLAccess.getRegId(uids);
		}
		
		if ((devices == null || devices.size() == 0) && !defaultMod) {
			status = "Message ignored as there is no device selected!";
		} else {

			//String run[] = req.getParameterValues(Attributes.ON_OFF_SWITCH);
			String run = params.get(Attributes.ON_OFF_SWITCH);
			//if (run != null && run.length >= 1) {
			if (run != null && run.equals("on")) {
				mRun = true;
			} else {
				mRun = false;
			}

			//String condition = req.getParameter(Attributes.CONDITION);
			String condition = params.get(Attributes.CONDITION);
			if (condition != null) {
				try {
					mCondition = Integer.parseInt(condition);
				} catch (NumberFormatException e) {
					mCondition = -1;
				}
			} else {
				mCondition = -1;
			}

			//String newImg[] = req.getParameterValues(Attributes.NEW_IMAGE);
			String newImg = params.get(Attributes.NEW_IMAGE);
			//if (newImg != null && newImg.length >= 1) {
			if (newImg != null && newImg.equals("on")) {
				mNewImage = true;
			} else {
				mNewImage = false;
			}

			//String position = req.getParameter(Attributes.POSITION);
			String position = params.get(Attributes.POSITION);
			if (position != null) {
				try {
					mPosition = Integer.parseInt(position);
				} catch (NumberFormatException e) {
					mPosition = -1;
				}
			} else {
				mPosition = -1;
			}

			//String theme = req.getParameter(Attributes.THEME);
			String theme = params.get(Attributes.THEME);
			if (theme != null) {
				try {
					mTheme = Integer.parseInt(theme);
				} catch (NumberFormatException e) {
					mTheme = -1;
				}
			} else {
				mTheme = -1;
			}

			//mDialogText = req.getParameter(Attributes.DIALOGTXT);
			mDialogText = params.get(Attributes.DIALOGTXT);
			if (mDialogText != null && mDialogText.equals("")) {
				mDialogText = null;
			}
			//mDialogTitle = req.getParameter(Attributes.DIALOGTITLE);
			mDialogTitle = params.get(Attributes.DIALOGTITLE);
			if (mDialogTitle != null && mDialogTitle.equals("")) {
				mDialogTitle = null;
			}

			//String littleInterval = req.getParameter(Attributes.LITTLE_INTERVAL);
			String littleInterval = params.get(Attributes.LITTLE_INTERVAL);
			if (littleInterval != null && !littleInterval.equals("")) {
				try {
					mLittleInterval = Integer.parseInt(littleInterval);
				} catch (NumberFormatException e) {
					mLittleInterval = -1;
				}
			} else {
				mLittleInterval = -1;
			}
			//String bigInterval = req.getParameter(Attributes.BIG_INTERVAL);
			String bigInterval = params.get(Attributes.BIG_INTERVAL);
			if (bigInterval != null && !bigInterval.equals("")) {
				try {
					mBigInterval = Integer.parseInt(bigInterval);
				} catch (NumberFormatException e) {
					mBigInterval = -1;
				}
			} else {
				mBigInterval = -1;
			}
			//String dataInterval = req.getParameter(Attributes.DATA_INTERVAL);
			String dataInterval = params.get(Attributes.DATA_INTERVAL);
			if (dataInterval != null && !dataInterval.equals("")) {
				try {
					mDataInterval = Integer.parseInt(dataInterval);
				} catch (NumberFormatException e) {
					mDataInterval = -1;
				}
			} else {
				mDataInterval = -1;
			}

			//mSurvey = req.getParameter(Attributes.SURVEY_URL);
			mSurvey = params.get(Attributes.SURVEY_URL);
			if (mSurvey != null && mSurvey.equals("")) {
				mSurvey = null;
			}
			
			System.out.println("This is my parameter :: " + PushMessages.EXTRA_NEW_IMAGE);

			if (defaultMod) {

				if (MySQLAccess.setNewDefault(mRun, mDialogTitle, mDialogText,
						mLittleInterval, mBigInterval, mDataInterval,
						mCondition, mTheme, mPosition)) {
					status = "Default values modified successfully.";
				} else {
					status = "Error when updtating default values.";
				}

			} else {

				mMessage = new Message.Builder()
						.addData(PushMessages.EXTRA_RUN, String.valueOf(mRun))
						.addData(PushMessages.EXTRA_CONDITION,
								String.valueOf(mCondition))
						.addData(PushMessages.EXTRA_POSITION,
								String.valueOf(mPosition))
						.addData(PushMessages.EXTRA_THEME,
								String.valueOf(mTheme))
						.addData(PushMessages.EXTRA_NEW_IMAGE,
								String.valueOf(mNewImage))
						.addData(PushMessages.EXTRA_DIALOG, mDialogText)
						.addData(PushMessages.EXTRA_TITLE, mDialogTitle)
						.addData(PushMessages.EXTRA_LITTLE_INTERVAL,
								String.valueOf(mLittleInterval))
						.addData(PushMessages.EXTRA_BIG_INTERVAL,
								String.valueOf(mBigInterval))
						.addData(PushMessages.EXTRA_DATA_INTERVAL,
								String.valueOf(mDataInterval))
						.addData(PushMessages.EXTRA_SURVEY, mSurvey).build();

				/*
				 * status = "Running: " + mRun + ", " + "Condition: " +
				 * mCondition + ", " + "Positive: " + mPositiveBt + ", " +
				 * "Negative: " + mNegativeBt + ", " + "Dialog: " + mDialogText
				 * + ", " + "LittleInterval: " + mLittleInterval + ", " +
				 * "BigInterval: " + mBigInterval + ", " + "Survey: " + mSurvey
				 * + ".";
				 */

				// send a multicast message using JSON
				// must split in chunks of 1000 devices (GCM limit)
				int total = devices.size();
				List<String> partialDevices = new ArrayList<String>(total);
				int counter = 0;
				int tasks = 0;
				for (int i = 0; i < total; i++) {
					counter++;
					partialDevices.add(devices.get(i));
					int partialSize = partialDevices.size();
					if (partialSize == MULTICAST_SIZE || counter == total) {
						asyncSend(partialDevices);
						partialDevices.clear();
						tasks++;
					}
				}
				status = "Asynchronously sending " + tasks
						+ " multicast messages to " + total + " devices";
			}

		}

		req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
		getServletContext().getRequestDispatcher("/home").forward(req, resp);
	}

	private void asyncSend(List<String> partialDevices) {
		// make a copy
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {

			public void run() {
				MulticastResult multicastResult;
				try {
					multicastResult = mSender.send(mMessage, devices, 5);
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error posting messages", e);
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					checkResult(result, regId);
				}
			}
		});
	}

	private void checkResult(Result result, String regId) {
		String messageId = result.getMessageId();
		if (messageId != null) {
			logger.fine("Succesfully sent message to device: " + regId
					+ "; messageId = " + messageId);

			System.out.println("Successfully Sent message to device " + regId);

			MySQLAccess.update(regId, mRun, mCondition, mDialogText,
					mLittleInterval, mBigInterval, mSurvey, mDialogTitle,
					mDataInterval, mPosition, mTheme);

			String canonicalRegId = result.getCanonicalRegistrationId();
			if (canonicalRegId != null) {
				// same device has more than on registration id:
				// update it
				if (MySQLAccess.update(regId, canonicalRegId)) {
					logger.info("canonicalRegId " + canonicalRegId);
				} else {
					logger.info("Error when updating canonicalRegId "
							+ canonicalRegId);
				}
			}
		} else {
			String error = result.getErrorCodeName();
			if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
				// application has been removed from device -
				// unregister it
				if (MySQLAccess.unregister(regId)) {
					logger.info("Unregistered device: " + regId);
				} else {
					logger.info("Error when unregistering device: " + regId);
				}
			} else {
				logger.severe("Error sending message to " + regId + ": "
						+ error);
			}
		}
	}
}
