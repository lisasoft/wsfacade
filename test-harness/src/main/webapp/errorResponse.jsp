<%
	String error = null;
	String errorAttributeName = request.getParameter("errorAttributeName");
	String format = request.getParameter("format");

	if(format == null) {
		format = "text/html";
	}
	
	if(errorAttributeName == null) {
		error = (String)request.getParameter("error");
	} else {
		error = (String)request.getSession().getAttribute(errorAttributeName);
	}

	response.setContentType(format);
	response.getOutputStream().print(error);
%>