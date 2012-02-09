/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.jasper.service.export;

import fr.paris.lutece.plugins.jasper.business.JasperReportHome;
import fr.paris.lutece.plugins.jasper.service.ILinkJasperReport;
import fr.paris.lutece.plugins.jasper.service.JasperConnectionService;
import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class OdtJasperRender implements ILinkJasperReport, Cloneable
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
            fr.paris.lutece.plugins.jasper.business.JasperReport report = JasperReportHome.findByPrimaryKey( strReportId,
                    plugin );
            String strPageDesc = report.getUrl(  );
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

            JROdtExporter exporter = new JROdtExporter(  );

            ByteArrayOutputStream streamReport = new ByteArrayOutputStream(  );
            exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
            exporter.setParameter( JRExporterParameter.OUTPUT_STREAM, streamReport );

            exporter.exportReport(  );
            byteArray = streamReport.toByteArray(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return byteArray;
    }

    public String getFileName( String strReportId )
    {
        return strReportId + ".odt";
    }

    public String getFileType(  )
    {
        return "odt";
    }

    public JRExporter getExporter( HttpServletRequest request,
        fr.paris.lutece.plugins.jasper.business.JasperReport report )
    {
        // TODO Auto-generated method stub
        return null;
    }
}
