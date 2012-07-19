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
package com.lisasoft.wsfacade.mappers.wmts;

public class WmtsConstants {
	public static final String COMMON_REQUEST_ORDER  = "Layer,Style,dimensions,TileMatrixSet,TileMatrix,TileCol,TileRow";
	
	public static final String GET_TILE_MODEL = "Layer,Style,TileMatrixSet,TileMatrix,TileCol,TileRow,Format,mime";
	public static final String GET_TILE_ORDER = COMMON_REQUEST_ORDER + ",Format";

	public static final String GET_TILE_RESPONSE_MODEL = "";
	public static final String GET_TILE_RESPONSE_TEMPLATE = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" \n" + 
		"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" + 
		"  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" + 
		"  xsi:schemaLocation=\"http://www.w3.org/2001/12/soap-envelope http://www.w3.org/2001/12/soap-envelope.xsd\">\n" + 
		"  <soap:Body>\n" + 
		"    <BinaryPayload xmlns=\"http://www.opengis.net/wmts/1.0.0\" \n" + 
		"      xsi:schemaLocation=\"http://www.opengis.net/wmts/1.0.0 ../wmtsBinaryPayload_response.xsd\">\n" + 
		"      <Format>%s</Format>\n" + 
		"      <PayloadContent><![CDATA[%s]]></PayloadContent>\n" + 
		"    </BinaryPayload>\n" + 
		"  </soap:Body>\n" + 
		"</soap:Envelope>";

	public static final String GET_FEATURE_INFO_MODEL = GET_TILE_MODEL + ",J,I,InfoFormat,infomime";
	public static final String GET_FEATURE_INFO_ORDER = COMMON_REQUEST_ORDER + ",J,I,InfoFormat";

	public static final String GET_FEATURE_INFO_RESPONSE_MODEL = "feature_info";
	public static final String GET_FEATURE_INFO_RESPONSE_TEMPLATE = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\"\n" + 
		"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
		"	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" + 
		"	xsi:schemaLocation=\"http://www.w3.org/2001/12/soap-envelope http://www.w3.org/2001/12/soap-envelope.xsd\">\n" + 
		"	<soap:Body>\n" + 
		"		<FeatureInfoResponse xmlns=\"http://www.opengis.net/wmts/1.0.0\"\n" + 
		"			xsi:schemaLocation=\"http://www.opengis.net/wmts/1.0.0 ../wmtsAnyType_response.xsd\">\n" + 
		"%s\n" + 
		"		</FeatureInfoResponse>\n" + 
		"	</soap:Body>\n" + 
		"</soap:Envelope>";


	
	public static final String GET_CAPABILITIES_MODEL = "name,service,version,capabilities";
	public static final String GET_CAPABILITIES_RESPONSE_TEMPLATE = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"%s";
	
	public static final String ERROR_RESPONSE_TEMPLATE = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" \n" + 
		"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" + 
		"  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" + 
		"  xsi:schemaLocation=\"http://www.w3.org/2001/12/soap-envelope http://www.w3.org/2001/12/soap-envelope.xsd\">\n" +
		"  <soap:Body>\n" +
		"    <soap:Fault>\n" +
		"      <soap:Code>\n" +
		"        <soap:Value>soap:Server</soap:Value>\n" +
		"      </soap:Code>\n" +
		"      <soap:Reason>\n" +
		"        <soap:Text>A server exception was encountered.</soap:Text>\n" +
		"      </soap:Reason>\n" +
		"      <soap:Detail>\n" +
		"        <ows:ExceptionReport xmlns:ows=\"http://www.opengis.net/ows/1.1\" \n" +  
		"          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/ows/1.1 \n" + 
		"          http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd\" version=\"1.0.0\" xml:lang=\"en\">\n" + 
		"          <ows:Exception exceptionCode=\"%s\">\n" + 
        "            <ows:ExceptionText>\n" + 
        "              <![CDATA[\n" +
		"%s\n" +
		"              ]]>\n" + 
		"            </ows:ExceptionText>\n" +
		"          </ows:Exception>\n" + 
		"        </ows:ExceptionReport>\n" +
		"      </soap:Detail>\n" +
		"    </soap:Fault>\n" +
		"  </soap:Body>\n" + 
		"</soap:Envelope>";


	public static final String SOAP_OPERATIONS_METADATA_REPLACE =
		"\n\n" + 
		"      <ows:Post xlink:href=\"%s\">\n" +
		"        <ows:Constraint name=\"PostEncoding\">\n" +
		"          <ows:AllowedValues>\n" +
		"            <ows:Value>SOAP</ows:Value>\n" +
		"          </ows:AllowedValues>\n" +
		"        </ows:Constraint>\n" +
		"      </ows:Post>\n";
	
	public static final String SOAP_WSDL_LINK_TEMPLATE = 
		"    <WSDL xlink:href=\"%s?WSDL\" />\n";
}
