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
package fr.paris.lutece.plugins.jasper.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for JasperReport objects
 */
public final class JasperReportHome
{
    // Static variable pointed at the DAO instance
    private static IJasperReportDAO _dao = (IJasperReportDAO) SpringContextService.getPluginBean( "jasper", "jasperReportDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private JasperReportHome( )
    {
    }

    /**
     * Create an instance of the jasperReport class
     * 
     * @param jasperReport
     *            The instance of the JasperReport which contains the informations to store
     * @param plugin
     *            the Plugin
     * @return The instance of jasperReport which has been created with its primary key.
     */
    public static JasperReport create( JasperReport jasperReport, Plugin plugin )
    {
        _dao.insert( jasperReport, plugin );

        return jasperReport;
    }

    /**
     * Update of the jasperReport which is specified in parameter
     * 
     * @param jasperReport
     *            The instance of the JasperReport which contains the data to store
     * @param plugin
     *            the Plugin
     * @return The instance of the jasperReport which has been updated
     */
    public static JasperReport update( JasperReport jasperReport, Plugin plugin )
    {
        _dao.store( jasperReport, plugin );

        return jasperReport;
    }

    /**
     * Remove the jasperReport whose identifier is specified in parameter
     * 
     * @param nJasperReportId
     *            The jasperReport Id
     * @param plugin
     *            the Plugin
     */
    public static void remove( int nJasperReportId, Plugin plugin )
    {
        _dao.delete( nJasperReportId, plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a jasperReport whose identifier is specified in parameter
     * 
     * @param nKey
     *            The jasperReport primary key
     * @param plugin
     *            the Plugin
     * @return an instance of JasperReport
     */
    public static JasperReport findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Load the data of all the jasperReport objects and returns them in form of a collection
     * 
     * @param plugin
     *            the Plugin
     * @return the collection which contains the data of all the jasperReport objects
     */
    public static Collection<JasperReport> getJasperReportsList( Plugin plugin )
    {
        return _dao.selectJasperReportsList( plugin );
    }

    /**
     *
     * @param strCode
     * @param plugin
     * @return
     */
    public static JasperReport findByCode( String strCode, Plugin plugin )
    {
        return _dao.loadByCode( strCode, plugin );
    }
}
