/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class HtmlJasperRender extends AbstractDefaultJasperRender
{
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

    public String getFileType(  )
    {
        return "html";
    }

    public JRExporter getExporter( HttpServletRequest request,
        fr.paris.lutece.plugins.jasper.business.JasperReport report )
    {
        JRHtmlExporter exporter = new JRHtmlExporter(  );

        Map imagesMap = new HashMap(  );
        request.getSession(  ).setAttribute( "IMAGES_MAP", imagesMap );

        exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, false );
        exporter.setParameter( JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, false );
        exporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false );
        exporter.setParameter( JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_PIXEL );

        // Save image Map to file system
        String strImageFolderPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_FILES_PATH );
        String strAbsoluteImagePath = AppPathService.getWebAppPath(  ) + strImageFolderPath + report.getUrl(  ) + "/" +
            JasperFileLinkService.INSTANCE.getKey( request );
        File imageFolder = new File( strAbsoluteImagePath );

        if ( imageFolder.exists(  ) == false )
        {
            imageFolder.mkdirs(  );
        }

        exporter.setParameter( JRHtmlExporterParameter.IMAGES_DIR, imageFolder );
        exporter.setParameter( JRHtmlExporterParameter.IMAGES_URI,
            "plugins/jasper/images/" + report.getUrl(  ) + "/" + JasperFileLinkService.INSTANCE.getKey( request ) + "/" );
        exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true );

        return exporter;
    }
}
