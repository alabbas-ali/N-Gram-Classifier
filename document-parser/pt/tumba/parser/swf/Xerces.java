package pt.tumba.parser.swf;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *  Utilities for using the Apache Xerces XML Parser
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Xerces {

	private static final Xerces _theInstance = new Xerces();

			private Xerces() {
			}

			public static Xerces getInstance() {
				return _theInstance;
			}

    /**
     *  Description of the Method
     *
     *@param  handler                           Description of the Parameter
     *@param  in                                Description of the Parameter
     *@exception  SAXException                  Description of the Exception
     *@exception  IOException                   Description of the Exception
     *@exception  ParserConfigurationException  Description of the Exception
     */
    public static void parse(DefaultHandler handler, InputStream in) throws SAXException, IOException, ParserConfigurationException {
        XMLReader parser = (SAXParserFactory.newInstance()).newSAXParser().getXMLReader();
        InputSource source = new InputSource(in);
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);
        parser.parse(source);
        in.close();
    }
}
