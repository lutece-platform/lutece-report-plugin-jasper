<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="jasper" scope="session" class="fr.paris.lutece.plugins.jasper.web.JasperLinkServiceJspBean" />

<% 
     response.sendRedirect( jasper.doInsertLink( request ) );
%>
