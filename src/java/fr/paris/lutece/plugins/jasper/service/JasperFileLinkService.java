/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.jasper.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.jasper.business.JasperReport;
import fr.paris.lutece.plugins.jasper.service.export.HtmlJasperRender;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public enum JasperFileLinkService
{
    /**
     * Implementation of a singleton instance
     */
    INSTANCE;
    /**
     * Initialize the JasperFile link service
     *
     */

    private static final String PARAMETER_REPORT_ID = "report_id";
    private static final String PARAMETER_REPORT_TYPE = "report_type";
    private static final String PARAMETER_DBPAGE = "dbpage";
    private static final String PARAMETER_JASPER_VALUE = "value";
    private static final String PARAMETER_JASPER_LIST = "list";
    HashMap<String, ILinkJasperReport> mapStrategies;

    public static String getLink( String strReportId, String strType )
    {
        FileTypeContext context = null;
        String fileLink = StringUtils.EMPTY;;
        
		try 
		{
			context = getFileTypeContext( strType );
			fileLink = context.getFileLink( strReportId );
		} 
		catch (Exception e) 
		{
			throw e;
		}

        return fileLink;
    }

    static FileTypeContext getFileTypeContext( String strType )
    {
        Collection<ILinkJasperReport> col = ExportFormatService.INSTANCE.getExportTypes( );
        FileTypeContext fileTypeContext = null;

        for ( ILinkJasperReport renderFormat : col )
        {
            if ( renderFormat.getFileType( ).equals( strType ) )
            {
                return new FileTypeContext( renderFormat );
            }
            else
            {
                fileTypeContext = new FileTypeContext( new HtmlJasperRender( ) );
            }
        }

        return fileTypeContext;
    }

    /**
     * Exports the files in a byte array
     * 
     * @param request
     *            The Http request
     * @return An array of byte which is the content of the archive
     */
    public static byte [ ] exportFile( HttpServletRequest request )
    {
        String strReportCode = request.getParameter( PARAMETER_REPORT_ID );
        String strType = request.getParameter( PARAMETER_REPORT_TYPE );
        return exportFile( strReportCode, strType, request );
    }

    public static byte [ ] exportFile( HttpServletRequest request, String strReportCode )
    {
        String strType = request.getParameter( PARAMETER_REPORT_TYPE );
        return exportFile( strReportCode, strType, request );
    }

    public static byte [ ] exportFile( String strReportCode, String strJasperType, HttpServletRequest request )
    {

        byte [ ] buffer = new byte [ 1024];

        try 
        {
			FileTypeContext context = getFileTypeContext( strJasperType );
			buffer = context.getBuffer( strReportCode, request );
		} 
        catch (Exception e) 
        {
			throw e;
		}

        return buffer;
    }

    public static byte [ ] exportFile( String strReportCode, String strJasperType, JRBeanCollectionDataSource dataSource, HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return exportFile( strReportCode, strJasperType, (JRDataSource) dataSource, request );
    }

    public static byte [ ] exportFile( String strReportCode, String strJasperType, JRDataSource dataSource, HttpServletRequest request )
    {

        byte [ ] buffer = new byte [ 1024];

        try 
        {
			FileTypeContext context = getFileTypeContext( strJasperType );
			buffer = context.getBuffer( strReportCode, request );
		} 
        catch (Exception e) 
        {
			throw e;
		}

        return buffer;
    }

    public static byte [ ] exportFile( JasperReport report, String strJasperType, JRBeanCollectionDataSource dataSource, Map<String, Object> parameters,
            HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return exportFile( report, strJasperType, (JRDataSource) dataSource, parameters, request );
    }

    public static byte [ ] exportFile( JasperReport report, String strJasperType, JRDataSource dataSource, Map<String, Object> parameters,
            HttpServletRequest request )
    {

        byte [ ] buffer = new byte [ 1024];
        
        try 
        {
			FileTypeContext context = getFileTypeContext( strJasperType );
			buffer = context.getBuffer( report, dataSource, parameters, request );
		} 
        catch (Exception e) 
        {
			throw e;
		}
        
        return buffer;
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getFileName( HttpServletRequest request )
    {

        FileTypeContext context = null;
        String fileName = StringUtils.EMPTY;;
        
		try 
		{
			String strReportCode = request.getParameter( PARAMETER_REPORT_ID );
			String strType = request.getParameter( PARAMETER_REPORT_TYPE );			
			
			context = getFileTypeContext( strType );
			fileName = context.getFileName( strReportCode );
		} 
		catch (Exception e) 
		{
			throw e;
		}

        return fileName;
    }

    /**
     * Extracts values from the Http request
     * 
     * @param request
     *            The Http request
     * @return A list of values
     */
    public List<String> getValues( HttpServletRequest request )
    {
        List<String> list = new ArrayList<String>( );
        int i = 0;
        String strValue = request.getParameter( PARAMETER_JASPER_VALUE + ( i + 1 ) );

        while ( strValue != null )
        {
            list.add( i, strValue );
            i++;
            strValue = request.getParameter( PARAMETER_JASPER_VALUE + ( i + 1 ) );
        }

        // Collections.sort( list, String.CASE_INSENSITIVE_ORDER );
        return list;
    }

    public List<String> getValuesElm( HttpServletRequest request )
    {
        List<String> list = new ArrayList<String>( );
        int i = 0;
        String strValue = request.getParameter( PARAMETER_JASPER_LIST + ( i + 1 ) );

        while ( strValue != null )
        {
            list.add( i, strValue );
            i++;
            strValue = request.getParameter( PARAMETER_JASPER_LIST + ( i + 1 ) );
        }

        // Collections.sort( list, String.CASE_INSENSITIVE_ORDER );
        return list;
    }

    /**
     * Gets the cache key
     * 
     * @param nMode
     *            The mode
     * @param request
     *            The HTTP request
     * @return The key
     */
    public String getKey( HttpServletRequest request )
    {
        String strReportId = request.getParameter( PARAMETER_REPORT_ID );
        strReportId = ( strReportId == null ) ? "" : strReportId;

        String strDBPageId = request.getParameter( PARAMETER_DBPAGE ); // Must be externalised
        strDBPageId = ( strDBPageId == null ) ? "" : strDBPageId;

        String strParameters = "";
        List<String> listValues = JasperFileLinkService.INSTANCE.getValues( request );

        for ( int i = 0; i < listValues.size( ); i++ )
        {
            strParameters += ( PARAMETER_JASPER_VALUE + ( i + 1 ) + "_" + listValues.get( i ) );
        }

        return strReportId + strDBPageId + "_" + strParameters;
    }
}
