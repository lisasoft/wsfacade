<%@page import="java.util.List"%>
<%@page import="com.lisasoft.wsfacade.proxies.Proxy"%>
<%@page import="com.lisasoft.wsfacade.proxies.ProxyManager" contentType="text/html;charset=UTF-8" language="java"%>
<%
	response.setHeader("Cache-Control", "max-age=0, no-store, must-revalidate, private");
response.setHeader("cache-request-directive", "no-cache");
response.setHeader("cache-response-directive", "no-cache");

response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="EN">
<head>
<link rel="stylesheet" href='css/lisasoft.css' type="text/css" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script type="text/javascript">
var names = new Array();
var descriptions = new Array();
var files = new Array();

$(document).ready(function() {

	var i = 0;
    names[i] = "Get - WSDL request";
	descriptions[i] = "Gets a WSDL document from the proxy.";
	files[i] = "?WSDL";

	i++;
	names[i] = "Get - GetCapabilities Request";
	descriptions[i] = "Gets the capabilities of a WFS service";
	files[i] = "?&service=WFS&version=2.0.0&request=GetCapabilities";

	i++;
	names[i] = "Get - GetFeature Request";
	descriptions[i] = "Gets 2 features of a WFS service using KVP";
	files[i] = "?request=getfeature&typename=tds:AerodromeBoundaryGeocurve&service=wfs&version=2.0.0&maxfeatures=2";

	i++;
	names[i] = "Post - GetFeature Request";
	descriptions[i] = "Gets 20 features of the WFS service";
	files[i] = "xml/get_features.xml";

	i++;
	names[i] = "Post - Get Capabilities Request";
	descriptions[i] = "Gets the capabilities of the WMTS service";
	files[i] = "xml/get_capabilities.xml";

	i++;
    names[i] = "Post - GetTile Request";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache.";
	files[i] = "xml/get_tile.xml"; 

	i++;
    names[i] = "Post - GetTile Request - dimensions example";
	descriptions[i] = "Requests a tile from a WMTS layer with dimension information.";
	files[i] = "xml/get_tile_dimensions.xml";

	i++;
    names[i] = "Post - GetTile Request - bad dimensions example";
	descriptions[i] = "Requests a tile from a WMTS layer with incorrect dimension information.";
	files[i] = "xml/get_tile_dimensions_bad.xml";

	i++;
    names[i] = "Post - GetTile Request - incorrectly ordered dimensions example";
	descriptions[i] = "Requests a tile from a WMTS layer with dimension information in the wrong order.";
	files[i] = "xml/get_tile_dimensions_bad_order.xml";

	i++;
    names[i] = "Post - GetTile Request - missing dimensions example";
	descriptions[i] = "Requests a tile from a WMTS layer with dimension information missing.";
	files[i] = "xml/get_tile_missing_dimensions.xml";

	i++;
    names[i] = "Post - GetTile Request - bad layer";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server with an incorrect layer name.";
	files[i] = "xml/get_tile_bad_layer.xml"; 

	i++;
    names[i] = "Post - GetTile Request - bad style";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server with an incorrect style.";
	files[i] = "xml/get_tile_bad_style.xml"; 

	i++;
    names[i] = "Post - GetTile Request - missing style";
	descriptions[i] = "Requests a tile from a WMTS layer with required style information missing.";
	files[i] = "xml/get_tile_missing_style.xml";

	i++;
    names[i] = "Post - GetTile Request - bad tile matrix set";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server with an incorrect tile matrix set.";
	files[i] = "xml/get_tile_bad_tile_matrix_set.xml"; 

	i++;
    names[i] = "Post - GetTile Request - missing tile matrix set";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server missing the tile matrix set.";
	files[i] = "xml/get_tile_missing_tile_matrix_set.xml"; 

	i++;
    names[i] = "Post - GetTile Request - bad tile matrix";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server with incorrect tile matrix information.";
	files[i] = "xml/get_tile_bad_tile_matrix.xml"; 

	i++;
    names[i] = "Post - GetTile Request - missing tile matrix";
	descriptions[i] = "Requests a tile from a WMTS enabled TileCache server missing the tile matrix.";
	files[i] = "xml/get_tile_missing_tile_matrix.xml"; 

	i++;
    names[i] = "Post - Example GetTile from OWS-6 WMTS ER Document";
	descriptions[i] = "An example SOAP GetTile request from the OWS-6 WMTS ER Document";
	files[i] = "xml/get_tile_ows6.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache";
	files[i] = "xml/get_feature_info.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request - dimensions example";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache with dimension information";
	files[i] = "xml/get_feature_info_dimensions.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request - bad layer";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache with an incorrect layer name";
	files[i] = "xml/get_feature_info_bad_layer.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request - bad J value";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache with a J value of more than the width of a tile";
	files[i] = "xml/get_feature_info_bad_j.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request - bad I value";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache with an I value of more than the height of a tile";
	files[i] = "xml/get_feature_info_bad_i.xml";

	i++;
    names[i] = "Post - GetFeatureInfo Request - bad info format";
	descriptions[i] = "Requests feature information from a WMTS enabled TileCache with a malformed info format";
	files[i] = "xml/get_feature_info_bad_info_format.xml";

	i++;
    names[i] = "Post - Example GetFeatureInfo from OWS-6 WMTS ER Document";
	descriptions[i] = "An example SOAP GetFeatureInfo request from the OWS-6 WMTS ER Document";
	files[i] = "xml/get_feature_info_ows6.xml";

	var options = "";
	for(i=0; i< names.length; i++) {
		options += '<option value="' + i + '">' + names[i] + " - " + descriptions[i] + '</option>';
	}
	$("select#requests").html(options);

	//$("select#requests").selectOptions("2");
	$("select#requests").val("2");
	changeRequest(2);

	options = "";

<%List proxies = ProxyManager.getProxies();
	for(int i = 0; i < proxies.size(); i++) {
		Proxy proxy = (Proxy)proxies.get(i);%>
	options += '<option value="<%=proxy.getProxyContextUrl()%>"><%=proxy.getName()%>: <%=proxy.getProxyContextUrl()%></option>';
<%
	}
%>
	$("select#proxy").html(options);
	$("select#proxy").val("0");
});

function changeRequest(id) {
	$("div#requestDesc").html("<h3>" + descriptions[id] + "</h3>");
	$("textarea#requestXML").val("");
	if(files[id].indexOf("?") == 0) {
		// populate the textarea with the value of the files entry
		$("textarea#requestXML").val(files[id]);
	} else {
		// load the file
		$.get(files[id], {}, function(xml) {$("textarea#requestXML").val(xml)}, "text");
	}
}
</script>
</head>
<body>
<div id="contentDivMainColumnTotalWidth">
	<table width="100%">
		<tr>
			<td align="left">
				<h1>${pom.name}</h1>
				<h2>Release: ${pom.version}</h2>
			</td>
			<td align="right">&nbsp;</td>
		</tr>
	</table>
	<div class="rightColumnTitle">Welcome</div>
	<table>
		<tbody>
			<tr>
				<td width="200">
				<h2>Resources</h2>
				<ul class="level2">
					<li class="leaf"><a href="index.jsp">About WS-Facade</a></li>
					<li class="leaf"><a href="testharness.jsp">Test Harness</a></li>
				</ul>
				
				</td>
				<td width="10"/>
				<td align="justify">
					<h2>About ${pom.name}</h2>
					<a target="_new" href="http://www.lisasoft.com"><b>LISAsoft</b></a> welcomes you to the ${pom.name}. 
				</td>
			</tr>
		</tbody>
	</table>
	
<h1>Example Proxied Requests</h1>

		<form method="post" action="requestSender.jsp" target="_blank" name="requester">
			<b>Proxy</b>
			<select id="proxy" name="proxy"></select><br/>
			<br/>
			<select id="requests" onchange="changeRequest(this.value)"></select>
			<br/>
			<div id="requestDesc"><h3>XML Sources may take a moment to load...</h3></div>
			<textarea id="requestXML" name="xml" cols="100" rows="20"></textarea>
			<input type="submit" value="Submit" />
		</form>
</div>
</body>
</html>