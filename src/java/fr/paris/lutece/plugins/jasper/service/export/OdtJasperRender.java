package fr.paris.lutece.plugins.jasper.service.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import fr.paris.lutece.plugins.jasper.business.JasperReportHome;
import fr.paris.lutece.plugins.jasper.service.ILinkJasperReport;
import fr.paris.lutece.plugins.jasper.service.JasperConnectionService;
import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class OdtJasperRender implements ILinkJasperReport,Cloneable
{
    private static final String PROPERTY_FILES_PATH = "jasper.files.path";
    private static final String PARAMETER_JASPER_VALUE = "value";

    public String getLink( String strReportId )
    {
        //returns the link of the file pointing to the pdf file
        return "jsp/site/plugins/jasper/DownloadFile.jsp?report_type=odt&report_id=" + strReportId;
    }

    public byte[] getBuffer( String strReportId, HttpServletRequest request )
    {
        byte[] byteArray = new byte[1024];

        try
        {
            Plugin plugin = PluginService.getPlugin( "jasper" );

            //get the report Id and construct the path to the corresponding jasper file
            fr.paris.lutece.plugins.jasper.business.JasperReport report = JasperReportHome.findByPrimaryKey( strReportId ,
                    plugin );
            String strPageDesc = report.getUrl(  ) ;
            String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            String strAbsolutePath = AppPathService.getWebAppPath(  ) + strDirectoryPath + strPageDesc;

            File reportFile = new File( strAbsolutePath );

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject( reportFile.getPath(  ) );

            Map parameters = new HashMap(  );
            List<String> listValues = JasperFileLinkService.INSTANCE.getValues( request );
            for ( int i = 0; i < listValues.size(  ); i++ )
            {
                parameters.put( PARAMETER_JASPER_VALUE + ( i + 1 ), listValues.get( i ) );
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters,
                    JasperConnectionService.getConnectionService( report.getPool(  ) ).getConnection(  ) );

            JROdtExporter exporter = new JROdtExporter();

            ByteArrayOutputStream streamReport = new ByteArrayOutputStream(  );
            exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
            exporter.setParameter( JRExporterParameter.OUTPUT_STREAM, streamReport );

            exporter.exportReport(  );
            byteArray = streamReport.toByteArray(  );
        }
        catch ( Exception e )
        {
        	AppLogService.error(e);
        }

        return byteArray;
    }

    public String getFileName( String strReportId )
    {
        return strReportId + ".odt";
    }

	public String getFileType() {
		
		return "odt";
	}
}
