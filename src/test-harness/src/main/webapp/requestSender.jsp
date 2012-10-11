<%@page import="java.io.InputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.OutputStreamWriter"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.lisasoft.wsfacade.utils.BinaryPayload"%>
<%@page import="com.lisasoft.wsfacade.utils.BinaryPayloadExtracter" contentType="text/html;charset=UTF-8" language="java"%>
<%

	String xml = request.getParameter("xml");
	String proxy = request.getParameter("proxy");
	String xmlResponse = "";
	String imageUrl = "";
	
	HttpURLConnection conn = null;

	if(xml.startsWith("?")) {
		// it's not XML, it's a URL query to send instead
		URL myServlet = new URL("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + proxy + xml);
		conn = (HttpURLConnection)myServlet.openConnection();
		conn.setDoOutput(true);
	} else {
		URL myServlet = new URL("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + proxy);
		conn = (HttpURLConnection)myServlet.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter writer =  new OutputStreamWriter(conn.getOutputStream());
		writer.write(xml);
		writer.flush();
		writer.close();
	}
%>
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
	<title>Web Service Facade Response</title>
	<link rel="stylesheet" href='css/lisasoft.css' type="text/css" />

	<link rel="stylesheet" href="libs/codemirror-2.34/lib/codemirror.css" />
    <script src="libs/codemirror-2.34/lib/codemirror.js"></script>
    <script src="libs/codemirror-2.34/mode/xml/xml.js"></script>

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
	
	<hr />

<%
	if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		while((line = in.readLine())!=null) {
			xmlResponse += line + "\n";
		}
		in.close();
		conn.disconnect();

		if(xmlResponse.contains("<BinaryPayload") || xmlResponse.contains("<wmts:BinaryPayload")) { //special case for images
			BinaryPayload payload = BinaryPayloadExtracter.extractBinaryPayload(xmlResponse);
			
			imageUrl = "base64decoder.jsp?mimetype=" + URLEncoder.encode(payload.format, "UTF-8") + "&dataAttributeName=wsf_data";

			// put the image data and the xml in the session 
			request.getSession().setAttribute("wsf_data",payload.payloadContent);	
			request.getSession().setAttribute("wsf_xml",xmlResponse);
		}
	}
	%>
		<h1><u>Web Service Facade Response: <%=conn.getResponseCode()%></u></h1>
		<br/>
	<% if (imageUrl != null && !"".equals(imageUrl)) { %>
		<img src="<%=imageUrl%>" />
	<%
		} else {
			%>
				<textarea cols="100" rows="30" id="code">
					<%=xmlResponse%>
				</textarea>
			<%
		}
%>
	<script>
      var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
        mode: {name: "xml", alignCDATA: true},
        lineNumbers: true
      });
    </script>
    </body>
</html>