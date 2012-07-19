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
package com.lisasoft.wsfacade.interpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.mappers.Mapper;
import com.lisasoft.wsfacade.models.Model;

/**
 * Interprets a HTTP requests or responses into a common model.
 * @author jgroffen
 *
 */
public abstract class HttpInterpreter {
	
    static final Logger log = Logger.getLogger(HttpInterpreter.class);
    
    protected Mapper mapper;
    
    HttpInterpreter(Mapper mapper) {
    	this.mapper = mapper;
    }
	
	public abstract Model interpretRequest(HttpServletRequest request) throws IOException;

	public abstract Model interpretResponse(HttpResponse response) throws IOException;

    protected static String LINE_SEPERATOR = "\n";

	static {
    	LINE_SEPERATOR = System.getProperty("line.separator");
	    if (LINE_SEPERATOR == null || LINE_SEPERATOR.length() <= 0) {
	    	LINE_SEPERATOR = "\n";
	    }
    }

	/**
	 * Helper method to read the contents of the response into a string.
	 * Also gives subclasses an opportunity to interpret the HTTP headers 
	 * by overriding getHeaders.
	 * @param response
	 * @return Textual body of the content.
	 */
	protected String readString(HttpResponse response) throws IOException {
	
		String result = "";
		
		// Get hold of the response entity
	    HttpEntity entity = response.getEntity();
	    
	    // If the response does not enclose an entity, there is no need
	    // to bother about connection release
	    if (entity != null) {
	    	result = EntityUtils.toString(entity);
	    }
	    
	    return(result);
	}

	protected String readString(HttpServletRequest request) throws IOException {
	
	    StringBuffer buf = new StringBuffer("");
	    BufferedReader bufReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	    String line = null;
	    try {
	        do {
	            line = bufReader.readLine();
	
	            if (line != null) {
	                buf.append(line);
	                buf.append(LINE_SEPERATOR);
	            }
	        } while (line != null);
	    } finally {
	        try {
	            if (bufReader != null)
	                bufReader.close();
	        } catch (IOException ioe) {
	            log.error("Unable to properly close the BufferedReader. Nothing we can do at this stage.", ioe);
	        }
	    }
	    
	    return(buf.toString());
	}

	/**
	 * Helper method to read the contents of the response into a byte array.
	 * @param response
	 * @return Byte array of the content.
	 */
	protected byte[] readBinary(HttpResponse response) throws IOException {
		
		byte[] result = null;
		
		// Get hold of the response entity
	    HttpEntity entity = response.getEntity();
	    
	    // If the response does not enclose an entity, there is no need
	    // to bother about connection release
	    if (entity != null) {
	    	result = EntityUtils.toByteArray(entity);
	    }
	    
	    return result;
	}

	protected byte[] readBinary(HttpServletRequest request) throws IOException {
		
		byte[] result = null;
	
		InputStream instream = request.getInputStream();
	    
	    if (instream != null) {
	        int i = (int)request.getContentLength();
	        if (i < 0) {
	            i = 4096;
	        }
	        ByteArrayBuffer buffer = new ByteArrayBuffer(i);
	        try {
	            byte[] tmp = new byte[4096];
	            int l;
	            while((l = instream.read(tmp)) != -1) {
	                buffer.append(tmp, 0, l);
	            }
	        } finally {
	            instream.close();
	        }
	        result = buffer.toByteArray();
	    }
	    
	    return result;
	}
}
