
<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="jasperPortlet" scope="session" class="fr.paris.lutece.plugins.jasper.web.portlet.JasperPortletJspBean" />

<%
    jasperPortlet.init( request, jasperPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( jasperPortlet.doCreate( request ) );
%>

