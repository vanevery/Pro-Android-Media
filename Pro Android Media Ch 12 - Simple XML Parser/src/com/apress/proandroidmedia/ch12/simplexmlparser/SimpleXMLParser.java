package com.apress.proandroidmedia.ch12.simplexmlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SimpleXMLParser extends Activity {

	XMLUser aUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String xml = "<?xml version=\"1.0\"?>\n" + "<user>\n"
				+ "<user-id>15</user-id>\n" + "<username>vanevery</username>\n"
				+ "<firstname>Shawn</firstname>\n"
				+ "<lastname>Van Every</lastname>\n" + "</user>\n";

		SAXParserFactory aSAXParserFactory = SAXParserFactory.newInstance();
		try {

			SAXParser aSAXParser = aSAXParserFactory.newSAXParser();
			XMLReader anXMLReader = aSAXParser.getXMLReader();

			UserXMLHandler aUserXMLHandler = new UserXMLHandler();
			anXMLReader.setContentHandler(aUserXMLHandler);

			anXMLReader.parse(new InputSource(new ByteArrayInputStream(xml
					.getBytes())));

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class UserXMLHandler extends DefaultHandler {

		static final int NONE = 0;
		static final int ID = 1;
		static final int FIRSTNAME = 2;
		static final int LASTNAME = 3;

		int state = NONE;

		static final String ID_ELEMENT = "user-id";
		static final String FIRSTNAME_ELEMENT = "firstname";
		static final String LASTNAME_ELEMENT = "lsatname";

		@Override
		public void startDocument() throws SAXException {
			Log.v("SimpleXMLParser", "startDocument");
			aUser = new XMLUser();
		}

		@Override
		public void endDocument() throws SAXException {
			Log.v("SimpleXMLParser", "endDocument");
			Log.v("SimpleXMLParser", "User Info: " + aUser.user_id + " "
					+ aUser.firstname + " " + aUser.lastname);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			Log.v("SimpleXMLParser", "startElement");
			if (localName.equalsIgnoreCase(ID_ELEMENT)) {
				state = ID;
			} else if (localName.equalsIgnoreCase(FIRSTNAME_ELEMENT)) {
				state = FIRSTNAME;
			} else if (localName.equalsIgnoreCase(LASTNAME_ELEMENT)) {
				state = LASTNAME;
			} else {
				state = NONE;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			Log.v("SimpleXMLParser", "endElement");

		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String stringChars = new String(ch, start, length);
			Log.v("SimpleXMLParser", stringChars);
			if (state == ID) {
				aUser.user_id += stringChars.trim();
				Log.v("SimpleXMLParser", "user_id:" + aUser.user_id);
			} else if (state == FIRSTNAME) {
				aUser.firstname += stringChars.trim();
				Log.v("SimpleXMLParser", "firstname:" + aUser.firstname);
			} else if (state == LASTNAME) {
				aUser.lastname += stringChars.trim();
				Log.v("SimpleXMLParser", "lastname:" + aUser.lastname);
			}
		}
	}

	class XMLUser {
		String user_id;
		String firstname;
		String lastname;

		public XMLUser() {
			user_id = "";
			firstname = "";
			lastname = "";
		}
	}
}
