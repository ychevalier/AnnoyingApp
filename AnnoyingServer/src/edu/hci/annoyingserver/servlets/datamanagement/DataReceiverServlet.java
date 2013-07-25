package edu.hci.annoyingserver.servlets.datamanagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.Utils;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.protocol.Data;

@SuppressWarnings("serial")
public class DataReceiverServlet extends BaseServlet {

	private static final boolean DEBUG_MODE = Utils.DEBUG_MODE;
	private static final String TAG = DataReceiverServlet.class.getSimpleName()
			+ ": ";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		//boolean isSuccess = true;

		if (DEBUG_MODE) {
			System.out.println(TAG + "New Data.");
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(req.getInputStream());
		} catch (Exception e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
			setFailureServer(resp);
			return;
		}

		doc.getDocumentElement().normalize();

		int uid = Integer.parseInt(doc.getDocumentElement().getAttribute(
				Data.ATT_UID));

		NodeList dialogList = doc.getElementsByTagName(Data.TAG_DIALOG);

		if (DEBUG_MODE) {
			System.out.println(TAG + "\tNew XML Sheet.");
		}

		for (int i = 0; i < dialogList.getLength(); i++) {

			if (DEBUG_MODE) {
				System.out.println(TAG + "\t\tNew Dialog.");
			}

			Node nDialogNode = dialogList.item(i);

			if (nDialogNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eDialogElement = (Element) nDialogNode;

				long start = Long.parseLong(eDialogElement
						.getAttribute(Data.ATT_START));
				int condition = Integer.parseInt(eDialogElement
						.getAttribute(Data.ATT_CONDITION));
				String title = eDialogElement.getAttribute(Data.ATT_TITLE);
				String text = eDialogElement.getAttribute(Data.ATT_TEXT);
				int theme = Integer.parseInt(eDialogElement.getAttribute(Data.ATT_THEME));
				int position = Integer.parseInt(eDialogElement.getAttribute(Data.ATT_POSITION));
				String image = eDialogElement.getAttribute(Data.ATT_IMAGE);
				String topImage = eDialogElement.getAttribute(Data.ATT_TOP_IMAGE);
				String bottomImage = eDialogElement.getAttribute(Data.ATT_BOTTOM_IMAGE);

				int dialogId = MySQLAccess.insertDialog(uid, start, condition, title, text, position, theme, image, topImage, bottomImage);

				if (dialogId != -1) {
					if (DEBUG_MODE) {
						System.out.println(TAG + "\t\tInserted.");
					}
					NodeList interactionList = eDialogElement
							.getElementsByTagName(Data.TAG_INTERACTION);
					for (int j = 0; j < interactionList.getLength(); j++) {
						if (DEBUG_MODE) {
							System.out.println(TAG + "\t\t\tNew Interaction.");
						}

						Node nInteractionNode = interactionList.item(j);

						if (nInteractionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eInteractionElement = (Element) nInteractionNode;

							long date = Long.parseLong(eInteractionElement
									.getAttribute(Data.ATT_DATETIME));
							int button = Integer.parseInt(eInteractionElement
									.getAttribute(Data.ATT_BUTTON));

							MySQLAccess.insertInteraction(dialogId, date,
									button);
						}
					}
				} else {
					if (DEBUG_MODE) {
						System.out.println(TAG + "\t\tNot inserted.");
					}
				}
			}
		}

		//if (isSuccess) {
			setSuccess(resp);
		//} else {
		//	setFailureServer(resp);
		//}
	}
}
