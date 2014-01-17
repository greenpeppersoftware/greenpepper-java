<%
	String redirectURL = request.getRequestURI().replaceAll("index.gsp", "") + "movie/list";
	response.sendRedirect(redirectURL);
%>