<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="image" scope="request" class="fr.paris.lutece.plugins.jasper.web.ViewGraph" />
<% 
 
	image.doGenerateGraph( request , response );
 
%>
