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
package com.lisasoft.wsfacade.proxies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class WmtsProxy extends Proxy {
    static final Logger log = Logger.getLogger(WmtsProxy.class);
    
    protected static final String WSDL_TEMPLATE = "/xml/wmts_wsdl_template.xml";

	protected void processProxyManagedUrl(URL host, String url, String serviceRequestType, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		if(url.equals("?WSDL")) {
			response.setContentType("text/xml");
			ServletOutputStream out = response.getOutputStream();
			
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(WmtsProxy.class.getResourceAsStream(WSDL_TEMPLATE)));
			
			StringBuffer buf = new StringBuffer("");

		    try {
	            String line;
		        do {
		            line = bufReader.readLine();
		
		            if (line != null) {
		                buf.append(line);
		                buf.append("\n");
		            }
		        } while (line != null);
		    } finally {
		        try {
		            if (bufReader != null)
		                bufReader.close();
		        } catch (IOException ioe) {
		            log.fatal(String.format("WSDL template document is missing! Should be here: %s.", WSDL_TEMPLATE), ioe);
		        }
		    }
			
			out.print(String.format(buf.toString(), host, name));
			out.flush();
			out.close();
		}
	}
}
