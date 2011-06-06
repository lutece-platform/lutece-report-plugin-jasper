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

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.File;

import java.sql.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class HtmlJasperRender implements ILinkJasperReport
{
    private static final String PROPERTY_FILES_PATH = "jasper.files.path";
    private static final String PROPERTY_IMAGES_FILES_PATH = "jasper.images.path";
    private static final String PARAMETER_JASPER_VALUE = "value";

    public byte[] getBuffer( String strReportId, HttpServletRequest request )
    {
        StringBuffer sb = new StringBuffer(  );
        Plugin plugin = PluginService.getPlugin( "jasper" );

        try
        {
            fr.paris.lutece.plugins.jasper.business.JasperReport report = JasperReportHome.findByPrimaryKey( strReportId,
                    plugin );

            String strPageDesc = report.getUrl(  );
            String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            String strAbsolutePath = AppPathService.getWebAppPath(  ) + strDirectoryPath + strPageDesc;

            File reportFile = new File( strAbsolutePath );

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject( reportFile.getPath(  ) );
            List<String> listValues = JasperFileLinkService.INSTANCE.getValues( request );
            Map parameters = new HashMap(  );

            for ( int i = 0; i < listValues.size(  ); i++ )
            {
            	parameters.put( PARAMETER_JASPER_VALUE + ( i + 1 ), listValues.get( i ) );
            }

            Connection conn = JasperConnectionService.getConnectionService( report.getPool(  ) ).getConnection(  );
            JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, conn );
            JasperConnectionService.getConnectionService( report.getPool(  ) ).freeConnection( conn );

            JasperConnectionService.getConnectionService( report.getPool(  ) ).freeConnection( conn );

            JRHtmlExporter exporter = new JRHtmlExporter(  );

            Map imagesMap = new HashMap(  );
            request.getSession(  ).setAttribute( "IMAGES_MAP", imagesMap );

            exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
            exporter.setParameter( JRExporterParameter.OUTPUT_STRING_BUFFER, sb );
            exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, false );
            exporter.setParameter( JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, false );
            exporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false );
            exporter.setParameter( JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_PIXEL );
            exporter.setParameter( JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-9");
            
            //Save image Map to file system
            String strImageFolderPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_FILES_PATH );
            String strAbsoluteImagePath = AppPathService.getWebAppPath(  ) + strImageFolderPath + strPageDesc + "/" +
                JasperFileLinkService.INSTANCE.getKey( request );
            File imageFolder = new File( strAbsoluteImagePath );

            if ( imageFolder.exists(  ) == false )
            {
                imageFolder.mkdirs(  );
            }

            exporter.setParameter( JRHtmlExporterParameter.IMAGES_DIR, imageFolder );
            exporter.setParameter( JRHtmlExporterParameter.IMAGES_URI,
                "plugins/jasper/" + strPageDesc + "/" + JasperFileLinkService.INSTANCE.getKey( request ) + "/" );
            exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true );
            exporter.exportReport(  );
            exporter.reset(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return sb.toString(  ).getBytes(  );
    }

    public String getFileName( String strReportId )
    {
    	return strReportId + ".html";
    }

    public String getLink( String strReportId )
    {
        return null;
    }

    public static void delete( File imageFolder )
    {
        if ( imageFolder.exists(  ) )
        {
            File[] listFiles = imageFolder.listFiles(  );

            for ( int i = 0; i < listFiles.length; i++ )
            {
                File file = listFiles[i];

                if ( file.isDirectory(  ) )
                {
                    delete( file );
                }

                file.delete(  );
            }
        }
    }
    
	public String getFileType() {
		
		return "html";
	}
	
}
