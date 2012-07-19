<%
	String xml = null;
	String xmlAttributeName = request.getParameter("xmlAttributeName");

	if(xmlAttributeName == null) {
		xml = (String)request.getParameter("xml");
	} else {
		xml = (String)request.getSession().getAttribute(xmlAttributeName);
	}
	response.setContentType("text/xml");
	response.getOutputStream().print(xml);
%>