package fr.paris.lutece.plugins.jasper.web;

import fr.paris.lutece.plugins.jasper.business.JasperReport;
import fr.paris.lutece.plugins.jasper.business.JasperReportHome;
import fr.paris.lutece.plugins.jasper.service.ExportFormatService;
import fr.paris.lutece.plugins.jasper.service.ILinkJasperReport;
import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBean;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class JasperLinkServiceJspBean extends InsertServiceJspBean implements InsertServiceSelectionBean
{
    /**
     *
     */
    private static final String TEMPLATE_FILE_TYPES_VAILABLE = "admin/plugins/jasper/file_type_available.html";
    private static final long serialVersionUID = -7460659353253479092L;
    private static final String PARAMETER_FILE_CHOICE = "file_choice";
    private static final String PARAMETER_REPORT_ID = "report_id";
    private static final String MARK_REPORTS = "reports";
    private static final String MARK_INPUT = "input";
    private static final String MARK_FILE_TYPES ="file_types";
    private static final String PARAMETER_INPUT = "input";

    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        String strInput = request.getParameter( PARAMETER_INPUT );
        Plugin plugin = PluginService.getPlugin( "jasper" );
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        Locale locale = AdminUserService.getLocale( request );
        ReferenceList reports = new ReferenceList(  );
        ReferenceList listFileTypes = new ReferenceList();
        
    	Map<String,ILinkJasperReport> mapClasses = ExportFormatService.INSTANCE.getExportTypes();
  	   Collection<ILinkJasperReport> col = mapClasses.values(  );
  	  
         for ( ILinkJasperReport renderFormat : col )
         {
        	 listFileTypes.addItem( renderFormat.getFileType(),renderFormat.getFileType());
          
         }
        Collection<JasperReport> reportsList = JasperReportHome.getJasperReportsList( plugin );
        for(JasperReport report : reportsList)
        {
        	reports.addItem( report.getDescription(), report.getDescription() );
        }
       
        model.put(MARK_REPORTS, reports);
        model.put( MARK_INPUT, strInput );
        model.put(MARK_FILE_TYPES, listFileTypes);

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FILE_TYPES_VAILABLE, locale, model );

        return template.getHtml(  );
    }

    /**
     * Insert the link into the editor
     * @param request The HTTP request
     * @return The code to insert
     */
    public String doInsertLink( HttpServletRequest request )
    {
        String strReportId = request.getParameter( PARAMETER_REPORT_ID );
        String strFileChoice = request.getParameter( PARAMETER_FILE_CHOICE );
        String strInput = request.getParameter( PARAMETER_INPUT );

        // Gets the locale of the user
        Locale locale = AdminUserService.getLocale( request );

        String strUrl = JasperFileLinkService.getLink( strReportId, strFileChoice );
        String strInsert = "<a href=\"" + strUrl + "\">" + strReportId + "</a>";

        return insertUrl( request, strInput, strInsert );
    }
}
