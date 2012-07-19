<%@page import="com.lisasoft.wsfacade.utils.Base64Coder%@page import="java.awt.image.BufferedImage"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%
	String base64;
	String dataAttributeName = request.getParameter("dataAttributeName");
	if(dataAttributeName == null) {
		base64 = request.getParameter("base64");
		base64 = base64.replaceAll("\\s","");
	} else {
		base64 = (String) request.getSession().getAttribute(dataAttributeName);
	}

	byte[] buffer = Base64Coder.decode(base64.toCharArray());
	ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
	BufferedImage image = ImageIO.read(inputStream);

	String mimetype = request.getParameter("mimetype");
	String format = mimetype.split("/")[1];

	response.setContentType(mimetype);
	ImageIO.write(image,format,response.getOutputStream());
%>