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
package fr.paris.lutece.plugins.jasper.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class FileTypeContext
{
    private ILinkJasperReport strategy;

    // Constructor
    public FileTypeContext( ILinkJasperReport strategy )
    {
        this.strategy = strategy;
    }

    public String getFileLink( String strReportCode )
    {
        return strategy.getLink( strReportCode );
    }

    public byte [ ] getBuffer( String strReportCode, HttpServletRequest request )
    {
        return strategy.getBuffer( strReportCode, request );
    }

    public byte [ ] getBuffer( String strReportCode, JRBeanCollectionDataSource dataSource, Map<String, Object> parameters, HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return getBuffer( strReportCode, (JRDataSource) dataSource, parameters, request );
    }

    public byte [ ] getBuffer( String strReportCode, JRDataSource dataSource, Map<String, Object> parameters, HttpServletRequest request )
    {
        return strategy.getBuffer( strReportCode, dataSource, parameters, request );
    }

    public byte [ ] getBuffer( fr.paris.lutece.plugins.jasper.business.JasperReport report, JRBeanCollectionDataSource dataSource,
            Map<String, Object> parameters, HttpServletRequest request )
    {
        // We override the methods instead of replacing them to ensure binary compatibility
        return getBuffer( report, (JRDataSource) dataSource, parameters, request );
    }

    public byte [ ] getBuffer( fr.paris.lutece.plugins.jasper.business.JasperReport report, JRDataSource dataSource, Map<String, Object> parameters,
            HttpServletRequest request )
    {
        return strategy.getBuffer( report, dataSource, parameters, request );
    }

    public String getFileName( String strReportId )
    {
        return strategy.getFileName( strReportId );
    }
}
