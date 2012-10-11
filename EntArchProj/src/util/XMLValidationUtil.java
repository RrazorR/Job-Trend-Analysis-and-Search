package util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Satyajeet
 * Class to validate a XML against given DTD and XSD
 */

public class XMLValidationUtil {

//	private static final String XML_FILE = "/Users/Raje02/Documents/linkedin.xml";
//	private static final String SCHEMA_FILE = "/Users/Raje02/Documents/linkedin.xsd";
	private static final String XML_FILE = "/Users/Raje02/Documents/Employeexy.xml";
	private static final String SCHEMA_FILE = "/Users/Raje02/Documents/Employee.dtd";
	/*
	 * Satyajeet: Set constant xsd to 	true -- xsd
	 * 									false-- dtd
	 */
	private static boolean xsd = false;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, TransformerException {

		/*
		 * Satyajeet: Validation using the java XML validation api
		 */
		Schema schema = null;
		
		//This part for XSD
		if(xsd){
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(new File(SCHEMA_FILE));

			Validator validator = schema.newValidator();

			try {
				validator.validate(new StreamSource(XML_FILE));
				System.out.println("Document is valid.");
			}
			catch (SAXException ex) {
				System.out.println("Document is not valid because ");
				System.out.println(ex.getMessage());
			}  
			System.out.println("Validation Complete!!");
		}
		
		
		//This part for DTD
		else{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
				//Ignore the fatal errors
				public void fatalError(SAXParseException exception)
				throws SAXException { }
				//Validation errors 
				public void error(SAXParseException e)
				throws SAXParseException {
					System.out.println("Error at " +e.getLineNumber() + " line.");
					System.out.println(e.getMessage());
//					System.exit(0);
				}
				//Show warnings
				public void warning(SAXParseException err)
				throws SAXParseException{
					System.out.println(err.getMessage());
				}
			});
			org.w3c.dom.Document xmlDocument = builder.parse(new FileInputStream(XML_FILE));
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(System.out);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, SCHEMA_FILE);
			transformer.transform(source, result);
			System.out.println("\n\nValidation Complete!!");
		}

	}

}
