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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import fr.paris.lutece.plugins.jasper.business.JasperReport;
import fr.paris.lutece.test.LuteceTestCase;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperFileLinkServiceTest extends LuteceTestCase
{
    @Test
    public void testExportFile( ) throws IOException
    {

        JasperReport reportJRBeanCollection = new JasperReport( );

        reportJRBeanCollection.setIdReport( 1 );

        Properties propTest = getTestProperties( );

        reportJRBeanCollection.setUrl( propTest.getProperty( "jasper.reportTestJRBeanUrl" ) );
        reportJRBeanCollection.setCode( propTest.getProperty( "jasper.reportTestJRBeanCode" ) );
        reportJRBeanCollection.setDateModification( new java.sql.Timestamp( new Date( ).getTime( ) ) );

        String reportType = propTest.getProperty( "jasper.reportTestGenerateType" );

        List<TestJavaDataSource> listTestJavaData = new ArrayList<>( );

        listTestJavaData.add( new TestJavaDataSource( ) );
        HashMap<String, Object> prop = new HashMap<>( );

        byte [ ] testExport = JasperFileLinkService.INSTANCE.exportFile( reportJRBeanCollection, reportType, new JRBeanCollectionDataSource( listTestJavaData ),
                new HashMap<String, Object>( ), null );

        try
        {
            File ff = new File( propTest.getProperty( "jasper.reportGenerateFilePath" ) ); // définir l'arborescence
            FileOutputStream sortie = new FileOutputStream( ff );
            sortie.write( testExport );
            sortie.close( );

        }
        catch( Exception e )
        {
        }

    }

    private Properties getTestProperties( )
    {
        Properties properties = null;
        try
        {

            URL url = Thread.currentThread( ).getContextClassLoader( ).getResource( "jasper-test.properties" );
            FileInputStream file = new FileInputStream( url.getPath( ) );

            properties = new Properties( );
            properties.load( file );

        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Unable to load test file : " + ex.getMessage( ) );
        }

        return properties;
    }

}
