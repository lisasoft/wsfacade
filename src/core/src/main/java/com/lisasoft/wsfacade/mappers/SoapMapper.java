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
package com.lisasoft.wsfacade.mappers;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lisasoft.wsfacade.models.Model;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.utils.SOAPConstants;

public class SoapMapper extends EntityMapper {
	
    static final Logger log = Logger.getLogger(SoapMapper.class);
    
    protected String startTag = null;

	protected SoapMapper() {}

	protected SoapMapper(String startTag){
		this.startTag = startTag;
	}

    public Model mapToModel(String source) throws IllegalArgumentException {
    	Model result = null;
    	try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);

			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(source));
			result = parse(xpp);
    	} catch(IllegalArgumentException iae) {
    		throw iae;
    	} catch (XmlPullParserException xppe) {
    		throw new IllegalArgumentException(String.format("XPP exception while parsing XML:\n\n %s", source), xppe);
		} catch (IOException ioe) {
    		throw new IllegalArgumentException(String.format("IOException while parsing XML:\n\n %s", source), ioe);
		}

		return result;
	}

	public String mapFromModel(Model model) throws UnsupportedModelException {
		String result = null;
		if(model.getProperties().containsKey("response")) {
			// TODO: This error reporting needs standards conforming information from the REST server so it can be properly reported here.
			result = String.format(SOAPConstants.ERROR_RESPONSE_TEMPLATE, "OperationNotSupported", model.getProperties().get("response"));
		} else {
			throw new UnsupportedModelException(String.format("%s cannot map from model %s - 'response' property expected.", getClass().getName(), model.getClass().getName()));
		}
		return(result);
	}

    protected Model parse(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {

    	Model model = null;

    	while(true) {
    		int eventType = xpp.nextTag();

    		if(eventType == XmlPullParser.START_TAG) {
                 String tag = xpp.getName();

                 if(startTag.equals(tag)) {
                	 if(model != null) {
                		 log.warn(String.format("The soap message could be parsed into more than one model. %s was ignored.", tag));
                	 } else {
                		 model = parseTags(xpp);
                	 }
                 } else if(eventType == XmlPullParser.END_TAG) {
                	 break;
                 }
    		}
    	}		
    	return model;
    }
	
    protected Model parseTags(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	Model model = new Model("");
    	
    	while(true) {
    		int eventType = xpp.nextTag();

    		if(eventType == XmlPullParser.START_TAG) {
    			String tag = xpp.getName();
    			
    			String val = xpp.getText();

				model.getProperties().put(tag, val);
                	 
             } else if(eventType == XmlPullParser.END_TAG) {
            	 break;
             }
    	}		
    	
    	return model;
    }
}
