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
package com.lisasoft.wsfacade.wmts.mappers;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lisasoft.wsfacade.mappers.SoapMapper;
import com.lisasoft.wsfacade.models.AbstractModel;
import com.lisasoft.wsfacade.models.CommonModel;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.utils.SoapConstants;
import com.lisasoft.wsfacade.wmts.models.GetFeatureInfoModel;
import com.lisasoft.wsfacade.wmts.models.GetFeatureInfoResponseModel;
import com.lisasoft.wsfacade.wmts.models.GetTileModel;
import com.lisasoft.wsfacade.wmts.models.GetTileResponseModel;

public class WmtsSoapMapper extends SoapMapper {

	static final Logger log = Logger.getLogger(WmtsSoapMapper.class);
	
	protected WmtsSoapMapper() {
		super(null);
	}
	
	protected WmtsSoapMapper(String startTag) {
		super(startTag);
	}

	public IModel mapToModel(String source) throws IllegalArgumentException {
		IModel result = null;
    	try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(source));
			result = parse(xpp);
    	} catch(IllegalArgumentException iae) {
    		throw iae;
    	} catch (XmlPullParserException xppe) {
    		throw new IllegalArgumentException(String.format("XPP exception while parsing XML: %s\nSource was:\n\n%s", xppe.getMessage(), source));
		} catch (IOException ioe) {
			if(source.length() == 0) {
				throw new IllegalArgumentException("IOException - no XML sent from client.", ioe);
			} else {
				throw new IllegalArgumentException(String.format("IOException while parsing XML: %s\nSource was:\n\n%s", ioe.getMessage(), source));
			}
		}
		
		return result;
	}
	
	public String mapFromModel(AbstractModel model) throws UnsupportedModelException {
		
		String result = null;
		
		if(model instanceof GetTileResponseModel) {
			GetTileResponseModel m = (GetTileResponseModel)model;
			
			if(m.getContentType().contains("image")) {
				result = String.format(SoapConstants.GET_TILE_RESPONSE_TEMPLATE, m.getContentType(), new String(Base64.encodeBase64(m.getBinarySource())));
			} else {
				// TODO: feature info responses end up here at the moment. This is ambiguous so we either 
				// remove the GetFeatureInfoResponseModel or go to the effort of differentiating between 
				// REST tile and FeatureInfo resources earlier than this point
				String responseBody = m.getTextSource();
				if(responseBody == null) {
					responseBody = new String(m.getBinarySource());
				}
				//if(responseBody == null) {
				//	throw new UnsupportedModelException("Unable to interpret response with contentType " + m.contentType);
				//} else {
					// remove the xml file descriptor bit as we'll be wrapping it in a soap message.
					if(responseBody.startsWith("<?")) {
						// chop to the first tag after the xml file descriptor bit
						responseBody = responseBody.substring(responseBody.indexOf("<", responseBody.indexOf("?>")));
					}
				//}
				result = String.format(SoapConstants.GET_FEATURE_INFO_RESPONSE_TEMPLATE, responseBody);
			}
			
		} else if(model instanceof GetFeatureInfoResponseModel) {
			GetFeatureInfoResponseModel m = (GetFeatureInfoResponseModel)model;
			result = String.format(SoapConstants.GET_FEATURE_INFO_RESPONSE_TEMPLATE, m.getProperties().get("response"));
			
		} else {
			if(model.getProperties().containsKey("capabilities")) {
				String responseBody = model.getProperties().get("capabilities");
				
				// remove the xml file descriptor bit as we'll be wrapping it in a soap message.
				if(responseBody.startsWith("<?")) {
					// chop to the first tag after the xml file descriptor bit
					responseBody = responseBody.substring(responseBody.indexOf("<", responseBody.indexOf("?>")));
				}
				responseBody = String.format(SoapConstants.GET_CAPABILITIES_RESPONSE_TEMPLATE, responseBody);
				result = injectCapabilities(responseBody, model.getProperties().get("host"));
				
			} else if(model.getProperties().containsKey("response")) {
				// TODO: This error reporting needs standards conforming information from the REST server so it can be properly reported here.
				result = String.format(SoapConstants.ERROR_RESPONSE_TEMPLATE, "OperationNotSupported", model.getProperties().get("response"));
			} else {
				throw new UnsupportedModelException(String.format("%s cannot map from model %s - 'response' property expected.", getClass().getName(), model.getClass().getName()));
			}
		}
			
		return result;
	}
	
	/**
	 * Parses an XML soap message.
	 * EG:
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope">
	 *   <soap:Body>
	 *     <GetTile service="WMTS" version="1.0.0">
	 *       <Layer>coastlines</Layer>
	 *       <Style>blue</Style>
	 *       <DimensionNameValue name="TIME">2007-06</Dimension>
	 *       <TileMatrixSet>coastlinesInCrs84</TileMatrixSet>
	 *       <Format>image/png</Format>
	 *       <TileMatrix>5e6</TileMatrix>
	 *       <TileCol>112</TileCol>
	 *       <TileRow>42</TileRow>
	 *     </GetTile>
	 *   </soap:Body>
	 * </soap:Envelope>
	 * @author jgroffen
	 * @throws IOException 
	 */
    protected IModel parse(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	
    	IModel model = null;

    	while(true) {
    		int eventType = xpp.next();

    		if(eventType == XmlPullParser.START_TAG) {
                 String tag = xpp.getName();

                 if("GetTile".equals(tag)) {
                	 if(model != null) {
                		 log.warn(String.format("The soap message could be parsed into more than one model. %s was ignored.", tag));
                	 } else {
                		 model = parseGetTile(xpp);
                		 break;
                	 }
                 } else if("GetFeatureInfo".equals(tag)) {
                	 if(model != null) {
                		 log.warn(String.format("The soap message could be parsed into more than one model. %s was ignored.", tag));
                	 } else {
                		 model = parseGetFeatureInfo(xpp);
                		 break;
                	 }
                 } else if("GetCapabilities".equals(tag)) {
                	 if(model != null) {
                		 log.warn(String.format("The soap message could be parsed into more than one model. %s was ignored.", tag));
                	 } else {
                		 model = parseGetCapabilities(xpp);
                		 break;
                	 }
                 }
    		}
    	}		
    	return model;
    }
	
    protected GetTileModel parseGetTile(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	return parseGetTile(xpp, null);
    }
    
    protected GetTileModel parseGetTile(XmlPullParser xpp, GetTileModel model) throws IllegalArgumentException, XmlPullParserException, IOException {
    	if(model == null) {
    		model = new GetTileModel();
    	}
    	
//		log.debug("XPP START_DOCUMENT=" + XmlPullParser.START_DOCUMENT);
//		log.debug("XPP END_DOCUMENT=" + XmlPullParser.END_DOCUMENT);
//		log.debug("XPP START_TAG=" + XmlPullParser.START_TAG);
//		log.debug("XPP END_TAG=" + XmlPullParser.END_TAG);
//		log.debug("XPP TEXT=" + XmlPullParser.TEXT);

    	while(true) {
    		int eventType = xpp.next();

//			log.debug("XPP - " + eventType);
    		
    		if(eventType == XmlPullParser.START_TAG) {
    			String tag = xpp.getName();
//    			log.debug("XPP - " + tag);

    			if("DimensionNameValue".equals(tag)) {
    				String name = xpp.getAttributeValue(null, "name");
    				String val = xpp.nextText();
//        			log.debug("XPP - " + tag + " = (" + name + "=" + val + ")");
    				model.dimensions.put(name, val);
    				model.dimensionsOrder.add(name);
    			} else if("Format".equals(tag)) {
    				String val = xpp.nextText(); 
//        			log.debug("XPP - " + tag + " = " + val);
    				model.getProperties().put("mime", val);
					try {
						model.getProperties().put(tag, val.split("/")[1]);
					} catch(Exception e) {
						// If for any reason we can't get the format out, complain bitterly.
						throw new IllegalArgumentException(String.format("Couldn't interpret the format: '%s'", tag));
					}
    			} else if(model.getProperties().containsKey(tag)) {
    				String val = xpp.nextText(); 
//        			log.debug("XPP - " + tag + " = " + val);
    				model.getProperties().put(tag, val);
    			} else {
    				throw new IllegalArgumentException(String.format("Tag '%s' with value '%s' was not expected.", xpp.getName(), xpp.nextText()));
    			}
    		} else if(eventType == XmlPullParser.END_TAG) {
    			if("GetTile".equals(xpp.getName())) {
    				break;
    			}
    		}
    	}
    	return model;
    }

    protected IModel parseGetCapabilities(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	IModel model = new CommonModel(SoapConstants.GET_CAPABILITIES_MODEL);
    	
		model.getProperties().put("name", xpp.getName());
		model.getProperties().put("service", xpp.getAttributeValue(null, "service"));
		
    	while(true) {
    		int eventType = xpp.next();

    		if(eventType == XmlPullParser.START_TAG) {
    			String tag = xpp.getName();

    			// This should look for the latest version in the list instead of the 
    			// last version specified. REST servers don't accept capability requests 
    			// for more than one version in a single request.
    			if("Version".equals(tag)) {
    				model.getProperties().put("version", xpp.nextText());
    			}

    		} else if(eventType == XmlPullParser.END_TAG) {
    			if("GetCapabilities".equals(xpp.getName())) {
    				break;
    			}
    		}
    	}

    	return model;
    }

    protected AbstractModel parseGetFeatureInfo(XmlPullParser xpp) throws IllegalArgumentException, XmlPullParserException, IOException {
    	GetFeatureInfoModel model = new GetFeatureInfoModel();
    	
    	while(true) {
    		int eventType = xpp.next();

//			log.debug("XPP - " + eventType);
    		
    		if(eventType == XmlPullParser.START_TAG) {
    			String tag = xpp.getName();
//    			log.debug("XPP - " + tag);

    			if("GetTile".equals(tag)) {
    				parseGetTile(xpp, model);
    			} else if("InfoFormat".equals(tag)) {
    				String val = xpp.nextText(); 
//        			log.debug("XPP - " + tag + " = " + val);
    				model.getProperties().put("infomime", val);
					try {
						model.getProperties().put(tag, val.split("/")[1]);
					} catch(Exception e) {
						// If for any reason we can't get the format out, complain bitterly.
						throw new IllegalArgumentException(String.format("Couldn't interpret the format: '%s'", tag));
					}
    			} else if(model.getProperties().containsKey(tag)) {
    				String val = xpp.nextText(); 
//        			log.debug("XPP - " + tag + " = " + val);
    				model.getProperties().put(tag, val);
    			} else {
    				throw new IllegalArgumentException(String.format("Tag '%s' with value '%s' was not expected.", xpp.getName(), xpp.nextText()));
    			}
    		} else if(eventType == XmlPullParser.END_TAG) {
    			if("GetFeatureInfo".equals(xpp.getName())) {
    				break;
    			}
    		}
    	}
    	return model;
    }

	/**
	 *  SOAP capabilities injection
	 * @param src
	 * @return
	 */
    protected String injectCapabilities(String src, String host) {
    	String result = src; 
    	
    	// get capabilities
    	int idx = result.indexOf("<ows:Operation name=\"GetCapabilities\">");
    	if(idx != -1) {
    		String new_content = String.format(SoapConstants.SOAP_OPERATIONS_METADATA_REPLACE, host);
    		int idx2 = result.indexOf("<ows:HTTP>", idx) + "<ows:HTTP>".length();
    		result = result.substring(0, idx2) + new_content + result.substring(idx2, result.length());
    	}

    	// get tile
    	idx = result.indexOf("<ows:Operation name=\"GetTile\">");
    	if(idx != -1) {
    		String new_content = String.format(SoapConstants.SOAP_OPERATIONS_METADATA_REPLACE, host);
    		int idx2 = result.indexOf("<ows:HTTP>", idx) + "<ows:HTTP>".length();
    		result = result.substring(0, idx2) + new_content + result.substring(idx2, result.length());
    	}
    	
    	// get feature info
    	idx = result.indexOf("<ows:Operation name=\"GetFeatureInfo\">");
    	if(idx != -1) {
    		String new_content = String.format(SoapConstants.SOAP_OPERATIONS_METADATA_REPLACE, host);
    		int idx2 = result.indexOf("<ows:HTTP>", idx) + "<ows:HTTP>".length();
    		result = result.substring(0, idx2) + new_content + result.substring(idx2, result.length());
    	}

    	// WSDL details
    	idx = result.indexOf("</Capabilities>");
    	if(idx != -1) {
    		String new_content = String.format(SoapConstants.SOAP_WSDL_LINK_TEMPLATE, host);
    		result = result.substring(0, idx) + new_content + result.substring(idx, result.length());
    	}
    	
    	return result;
    }
}
