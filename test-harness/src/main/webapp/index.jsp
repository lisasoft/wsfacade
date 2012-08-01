<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
			response.setHeader("Cache-Control",
			"max-age=0, no-store, must-revalidate, private");
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

<h1>${pom.name}</h1>

<p>This project provides a HTTP proxy service that will translate communication between a client and a server.</p>

<p>An example use is when a client only understands OGC WMTS SOAP and 
wants to talk to a particular WMTS, but this service only understands OGC 
WMTS REST.</p>

<p>This proxy can act as in intermediary that will:</p>
<ol>
	<li>Convert incoming SOAP requests to REST.</li>
	<li>Forward the REST request to the target REST service.</li> 
	<li>Interpret the REST response into a SOAP response.</li>
	<li>Forward the interpreted SOAP response to the request originator.</li>
	<li>Being OGC aware, if the original SOAP request was for capabilities 
	then this service will inject SOAP capabilities into the response.</li>
</ol>

<p>While the above example translates between a SOAP client and a REST service, 
the goal is that this service interpretation proxy is extensible and can be 
used for interpretation between numerous standard (and not so standard) HTTP 
encodings, including OGC flavour HTTP Get RPC.</p> 
	
</div>
</body>
</html>