package util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class URLConnectionUtil {

	public static Document extractDomFromWebSource(String inputurl, boolean isSecureConnection) throws Exception {

		URL url = new URL(inputurl);

		HttpsURLConnection connection_s = null;
		HttpURLConnection connection = null;
		InputStream xml=null;

		try {
			if (!isSecureConnection) {
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/xml");

				xml = connection.getInputStream();
			} else {
				connection_s = (HttpsURLConnection) url.openConnection();
				// connection_s.setRequestMethod("GET");
				// connection_s.setRequestProperty("Accept", "application/xml");

				xml = connection_s.getInputStream();

			}

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(xml);
			// org.w3c.dom.Document doc = db.parse(blob);
			return doc;
		} catch (Exception e) {
			if (connection != null) {
				connection.disconnect();
			}
			if (connection_s != null) {
				connection_s.disconnect();
			}
			if (xml != null) {
				xml.close();
			}
			e.printStackTrace();
			Thread.sleep(100);
			throw e;
		}
	}

	public static NodeList runXPathOverDOM(org.w3c.dom.Document doc, String query) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		XPathExpression xPathExpression = xPath.compile(query);
		NodeList nodes = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
		return nodes;
	}

}
