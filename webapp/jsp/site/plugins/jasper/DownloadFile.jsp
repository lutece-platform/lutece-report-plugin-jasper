<%@ page import="fr.paris.lutece.portal.service.util.AppLogService"%>
<%@ page import="fr.paris.lutece.plugins.jasper.service.JasperFileLinkService"%>
<%
	try
	{
		byte[] fileContent = JasperFileLinkService.exportFile(request);
		String strFileName = JasperFileLinkService.getFileName(request);
		if (fileContent != null)
		{
		
			//the header and also the names set by which user will be prompted to save
			response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
			String strMimetype = application.getMimeType(strFileName);
			response.setContentType( ( strMimetype != null ) ? strMimetype : "application/octet-stream" );
			response.setHeader("Cache-Control", "must-revalidate");
			response.getOutputStream().write(fileContent);
		}
	} 
	catch (Exception e)
	{
		AppLogService.error(e.getMessage(), e);
	}
	finally{
		response.getOutputStream().flush();
	}
%>
