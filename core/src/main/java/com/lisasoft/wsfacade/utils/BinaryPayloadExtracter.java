/*
 * Copyright 2012 LISAsoft - lisasoft.com. 
 * All rights reserved.
 *
 * This file is part of Web Services Facade.
 *
 * Web Services Facade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Web Services Facade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Web Services Facade.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lisasoft.wsfacade.utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class BinaryPayloadExtracter {
	
	static final Logger log = Logger.getLogger(BinaryPayloadExtracter.class);
	
	public static BinaryPayload extractBinaryPayload(String xml) {
		
		BinaryPayload result = null;
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
				
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			result = parse(xpp);
    	} catch(IllegalArgumentException iae) {
    		throw iae;
    	} catch (XmlPullParserException xppe) {
    		throw new IllegalArgumentException(String.format("XPP exception while parsing XML:\n\n %s", xml), xppe);
		} catch (IOException ioe) {
			if(xml.length() == 0) {
				throw new IllegalArgumentException("IOException - no XML to parse.", ioe);
			} else {
				throw new IllegalArgumentException(String.format("IOException while parsing XML:\n\n %s", xml), ioe);
			}
		}
		
		return result;
	}
	
    protected static BinaryPayload parse(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	
    	BinaryPayload result = new BinaryPayload();
    	
		int eventType = -1;
		
		// log.debug("XPP - start parsing");
    	
		while(eventType != XmlPullParser.END_DOCUMENT) {
    		eventType = xpp.next();

    		if(eventType == XmlPullParser.START_TAG) {
                 String tag = xpp.getName();

                 if("Format".equals(tag)) {
                	 result.format = xpp.nextText();
                	 // log.debug("XPP - Format: " + result.format);
                 } else if("PayloadContent".equals(tag)) {
                	 result.payloadContent = xpp.nextText();
                	 // log.debug("XPP - BinaryPayload: " + result.payloadContent);
                	 // A BinaryPayload must have a format then a payload content, so 
                	 // if we got a payload content we have already parsed the foramt, 
                	 // and we are done.
            		 break;
                 }
    		}
    	}		

		// log.debug("XPP - finish parsing");

		return result;
    }
}
