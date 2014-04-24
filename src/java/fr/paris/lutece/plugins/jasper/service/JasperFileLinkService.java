/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.jasper.service.export.HtmlJasperRender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
    HashMap<String,ILinkJasperReport> mapStrategies;

    public static String getLink( String strReportId, String strType )
    {
        FileTypeContext context = getFileTypeContext( strType );

        return context.getFileLink( strReportId );
    }

    static FileTypeContext getFileTypeContext( String strType )
    {
        Map<String, ILinkJasperReport> mapClasses = ExportFormatService.INSTANCE.getExportTypes(  );
        Collection<ILinkJasperReport> col = mapClasses.values(  );
        FileTypeContext fileTypeContext = null;

        for ( ILinkJasperReport renderFormat : col )
        {
            if ( renderFormat.getFileType(  ).equals( strType ) )
            {
                return new FileTypeContext( renderFormat );
            }
            else
            {
                fileTypeContext = new FileTypeContext( new HtmlJasperRender(  ) );
            }
        }

        return fileTypeContext;
    }

    /**
     * Exports the files in a byte array
     * @param request The Http request
     * @return An array of byte which is the content of the archive
     */
    public static byte[] exportFile( HttpServletRequest request )
    {
        String strReportId = request.getParameter( PARAMETER_REPORT_ID );
        String strType = request.getParameter( PARAMETER_REPORT_TYPE );
        FileTypeContext context = getFileTypeContext( strType );

        byte[] buffer = new byte[1024];

        buffer = context.getBuffer( strReportId, request );

        return buffer;
    }

    public static byte[] exportFile( HttpServletRequest request, String strJasperName )
    {
        String strReportId = strJasperName;
        String strType = request.getParameter( PARAMETER_REPORT_TYPE );

        byte[] buffer = new byte[1024];

        FileTypeContext context = getFileTypeContext( strType );
        buffer = context.getBuffer( strReportId, request );

        return buffer;
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getFileName( HttpServletRequest request )
    {
        String strReportId = request.getParameter( PARAMETER_REPORT_ID );
        String strType = request.getParameter( PARAMETER_REPORT_TYPE );
        FileTypeContext context = getFileTypeContext( strType );

        return context.getFileName( strReportId );
    }

    /**
     * Extracts values from the Http request
     * @param request The Http request
     * @return A list of values
     */
    public List<String> getValues( HttpServletRequest request )
    {
        List<String> list = new ArrayList<String>(  );
        int i = 0;
        String strValue = request.getParameter( PARAMETER_JASPER_VALUE + ( i + 1 ) );

        while ( strValue != null )
        {
            list.add( i, strValue );
            i++;
            strValue = request.getParameter( PARAMETER_JASPER_VALUE + ( i + 1 ) );
        }

        //Collections.sort( list, String.CASE_INSENSITIVE_ORDER );
        return list;
    }

    /**
     * Gets the cache key
     * @param nMode The mode
     * @param request The HTTP request
     * @return The key
     */
    public String getKey( HttpServletRequest request )
    {
        String strReportId = request.getParameter( PARAMETER_REPORT_ID );
        strReportId = ( strReportId == null ) ? "" : strReportId;

        String strDBPageId = request.getParameter( PARAMETER_DBPAGE ); //Must be externalised
        strDBPageId = ( strDBPageId == null ) ? "" : strDBPageId;

        String strParameters = "";
        List<String> listValues = JasperFileLinkService.INSTANCE.getValues( request );

        for ( int i = 0; i < listValues.size(  ); i++ )
        {
            strParameters += ( PARAMETER_JASPER_VALUE + ( i + 1 ) + "_" + listValues.get( i ) );
        }

        return strReportId + strDBPageId + "_" + strParameters;
    }
}
