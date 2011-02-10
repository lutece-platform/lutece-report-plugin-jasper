

<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="jasperReport" scope="session" class="fr.paris.lutece.plugins.jasper.web.JasperJspBean" />

<% jasperReport.init( request, jasperReport.RIGHT_MANAGE_JASPER ); %>
<%= jasperReport.getManageFileTypes ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

