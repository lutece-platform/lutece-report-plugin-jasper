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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.jasper.business.JasperReportHome;
import fr.paris.lutece.plugins.jasper.service.ILinkJasperReport;
import fr.paris.lutece.plugins.jasper.service.JasperConnectionService;
import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Abstract class used to generate a Jasper report
 *
 */
public abstract class AbstractDefaultJasperRender implements ILinkJasperReport, Cloneable
{
    protected static final String PLUGIN_NAME = "jasper";
    protected static final String PROPERTY_FILES_PATH = "jasper.files.path";
    protected static final String PROPERTY_IMAGES_FILES_PATH = "jasper.images.path";
    protected static final String PROPERTY_EXPORT_CHARACTER_ENCODING = "jasper.export.characterEncoding";
    protected static final String PROPERTY_FILES_ROOT_PATH = "jasper.files.root.path";
    protected static final String PROPERTY_IMAGES_ROOT_PATH = "jasper.images.root.path";
    protected static final String PARAMETER_JASPER_VALUE = "value";
    protected static final String PARAMETER_JASPER_IMAGE_DIRECTORY = "imageDirectory";
    protected static final String PARAMETER_JASPER_SUB_REPORT_DIRECTORY = "SUBREPORT_DIR";

    protected static final String REGEX_ID = "^[\\d]+$";
    protected static final String PATH_SEPARATOR = "/";
    private static final String SESSION_DATA_SOURCE = "dataSource";
    private static final String URL_PATTERN = "jsp/site/plugins/jasper/DownloadFile.jsp?report_type={0}&report_id={1}";
    private static final String FILE_EXTENSION_DELIMITER = ".";

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLink( String strReportId )
    {
        return MessageFormat.format( URL_PATTERN, getFileType( ), strReportId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getFileName( String strReportCode )
    {
        return strReportCode + FILE_EXTENSION_DELIMITER + getFileType( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public byte [ ] getBuffer( String strCode, HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_NAME );
        JRDataSource dataSource = null;
        fr.paris.lutece.plugins.jasper.business.JasperReport report = null;
        HttpSession session = request.getSession( false );

        if ( session != null )
        {
            dataSource = (JRDataSource) session.getAttribute( SESSION_DATA_SOURCE );
        }
        report = JasperReportHome.findByCode( strCode, plugin );
        List<String> listValues = JasperFileLinkService.INSTANCE.getValues( request );
        Map<String, Object> parameters = new HashMap<String, Object>( );

        for ( int i = 0; i < listValues.size( ); i++ )
        {
            parameters.put( PARAMETER_JASPER_VALUE + ( i + 1 ),
                    listValues.get( i ).matches( REGEX_ID ) ? Integer.parseInt( listValues.get( i ) ) : listValues.get( i ) );
        }

        return getBuffer( report, dataSource, parameters, request );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public byte [ ] getBuffer( String strCode, JRBeanCollectionDataSource dataSource, Map<String, Object> parameters, HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return getBuffer( strCode, (JRDataSource) dataSource, parameters, request );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public byte [ ] getBuffer( String strCode, JRDataSource dataSource, Map<String, Object> parameters, HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_NAME );
        fr.paris.lutece.plugins.jasper.business.JasperReport report = JasperReportHome.findByCode( strCode, plugin );
        return getBuffer( report, dataSource, parameters, request );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public byte [ ] getBuffer( fr.paris.lutece.plugins.jasper.business.JasperReport report, JRBeanCollectionDataSource dataSource,
            Map<String, Object> parameters, HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return getBuffer( report, (JRDataSource) dataSource, parameters, request );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public byte [ ] getBuffer( fr.paris.lutece.plugins.jasper.business.JasperReport report, JRDataSource dataSource, Map<String, Object> parameters,
            HttpServletRequest request )
    {
        byte [ ] byteArray = new byte [ 1024];
        Connection connection = null;
        try
        {

            String strPageDesc = report.getUrl( );
            String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            
            String strRootFilesPath = AppPropertiesService.getProperty( PROPERTY_FILES_ROOT_PATH );
            String strRootImagesPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_ROOT_PATH );

            String strRootFilesDirectory = StringUtils.isNoneBlank( strRootFilesPath ) ? strRootFilesPath : AppPathService.getWebAppPath( );
            String strRootImagesDirectory = StringUtils.isNoneBlank( strRootImagesPath ) ? strRootImagesPath : AppPathService.getWebAppPath( );
            
            String strAbsolutePath = strRootFilesDirectory + strDirectoryPath + strPageDesc;

            String strSubReportPath = strRootFilesDirectory + strDirectoryPath + report.getCode( ) + "/";

            File reportFile = new File( strAbsolutePath );

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject( reportFile );

            String strImageDirectoryPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_FILES_PATH );
            String strImageDirectoryAbsolutePath = new StringBuffer( strRootImagesDirectory ).append( strImageDirectoryPath )
                    .append( report.getCode( ) ).append( PATH_SEPARATOR ).toString( );
            parameters.put( PARAMETER_JASPER_IMAGE_DIRECTORY, strImageDirectoryAbsolutePath );
            parameters.put( PARAMETER_JASPER_SUB_REPORT_DIRECTORY, strSubReportPath );

            JasperPrint jasperPrint = null;
            if ( dataSource == null )
            {
                connection = JasperConnectionService.getConnectionService( report.getPool( ) ).getConnection( );
                jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, connection );
            }
            else
            {
                jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, dataSource );
            }

            byteArray = getData( request, report, jasperPrint );

        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        finally
        {
            if ( connection != null )
            {
                if ( report != null )
                {
                    try
                    {
                        JasperConnectionService.getConnectionService( report.getPool( ) ).freeConnection( connection );
                    }
                    catch( Exception e )
                    {
                        AppLogService.error( e.getMessage( ), e );
                        try
                        {
                            connection.close( );
                        }
                        catch( SQLException s )
                        {
                            AppLogService.error( s.getMessage( ), s );
                        }
                    }
                }
                try
                {
                    connection.close( );
                }
                catch( SQLException s )
                {
                    AppLogService.error( s.getMessage( ), s );
                }
            }
        }

        return byteArray;
    }

    /**
     * Gives data generated by Jasper
     * 
     * @param request
     *            the request
     * @param report
     *            the Jasper report
     * @param jasperPrint
     *            the JasperPrint
     * @return the data as array of bytes
     * @throws JRException
     *             if there is an exception during the treatment
     */
    protected abstract byte [ ] getData( HttpServletRequest request, fr.paris.lutece.plugins.jasper.business.JasperReport report, JasperPrint jasperPrint )
            throws JRException;
}
