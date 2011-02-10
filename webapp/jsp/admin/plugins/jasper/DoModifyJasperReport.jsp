

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="jasperReport" scope="session" class="fr.paris.lutece.plugins.jasper.web.JasperJspBean" />

<%
jasperReport.init( request, jasperReport.RIGHT_MANAGE_JASPER );
    response.sendRedirect( jasperReport.doModifyJasperReport( request ) );
%>

