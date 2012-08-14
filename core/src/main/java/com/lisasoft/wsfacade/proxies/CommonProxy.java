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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.utils.Constants;
import com.lisasoft.wsfacade.utils.PropertiesUtil;

public class CommonProxy extends Proxy {
    static final Logger log = Logger.getLogger(CommonProxy.class);
    
	protected void processProxyManagedUrl(URL host, String url, String serviceRequestType, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		if(url.equals(Constants.WSDL)) {
			response.setContentType(PropertiesUtil.getProperty(Constants.TYPE_TEXT_XML));
			ServletOutputStream out = response.getOutputStream();
			String templateFile = PropertiesUtil.getProperty(Constants.WSDL_TEMPLATE);
			String absolutePath = request.getSession().getServletContext().getRealPath(templateFile);
			String wsdlTemplate = FileUtils.readFileToString(new File(absolutePath));
			out.print(String.format(wsdlTemplate, host, name));
			out.flush();
			out.close();
		}
	}
}
