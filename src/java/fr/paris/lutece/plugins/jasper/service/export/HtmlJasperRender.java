/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.FileHtmlResourceHandler;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.type.HtmlSizeUnitEnum;

/**
 * This class generate a Jasper report in HTML format
 *
 */
public class HtmlJasperRender extends AbstractDefaultJasperRender
{
    private static final String FILE_TYPE = "html";
    private static final String SRC_TAG_IMAGE_PATH_PREFIX = "plugins/jasper/images/";
    private static final String SRC_TAG_IMAGE_PATH_SUFFIX = "{0}";

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLink( String strReportId )
    {
        return null;
    }

    /**
     * Deletes the folder containing the images
     * 
     * @param imageFolder
     *            the directory
     */
    public static void delete( File imageFolder )
    {
        if ( imageFolder.exists( ) )
        {
            File [ ] listFiles = imageFolder.listFiles( );

            for ( int i = 0; i < listFiles.length; i++ )
            {
                File file = listFiles [i];

                if ( file.isDirectory( ) )
                {
                    delete( file );
                }

                file.delete( );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getFileType( )
    {
        return FILE_TYPE;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected byte [ ] getData( HttpServletRequest request, fr.paris.lutece.plugins.jasper.business.JasperReport report, JasperPrint jasperPrint )
            throws JRException
    {

        HtmlExporter exporter = new HtmlExporter( );
        StringBuffer sb = new StringBuffer( );

        SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration( );
        configuration.setWhitePageBackground( false );
        configuration.setSizeUnit( HtmlSizeUnitEnum.PIXEL );
        exporter.setConfiguration( configuration );

        // Save image Map to file system
        String strImageFolderPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_FILES_PATH );
        String strAbsoluteImagePath = new StringBuffer( AppPathService.getWebAppPath( ) ).append( strImageFolderPath ).append( report.getUrl( ) )
                .append( PATH_SEPARATOR ).append( JasperFileLinkService.INSTANCE.getKey( request ) ).toString( );
        File imageFolder = new File( strAbsoluteImagePath );

        if ( !imageFolder.exists( ) )
        {
            imageFolder.mkdirs( );
        }

        exporter.setExporterInput( new SimpleExporterInput( jasperPrint ) );

        SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput( sb );
        HtmlResourceHandler imageHandler = new FileHtmlResourceHandler( imageFolder, new StringBuffer( SRC_TAG_IMAGE_PATH_PREFIX ).append( report.getUrl( ) )
                .append( PATH_SEPARATOR ).append( JasperFileLinkService.INSTANCE.getKey( request ) ).append( PATH_SEPARATOR )
                .append( SRC_TAG_IMAGE_PATH_SUFFIX ).toString( ) );
        exporterOutput.setImageHandler( imageHandler );
        exporter.setExporterOutput( exporterOutput );

        exporter.exportReport( );

        exporter.reset( );

        return sb.toString( ).getBytes( );
    }
}
