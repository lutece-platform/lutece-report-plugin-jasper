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

import java.util.Collection;

/**
 * IJasperReportDAO Interface
 */
public interface IJasperReportDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param jasperReport
     *            instance of the JasperReport object to inssert
     * @param plugin
     *            the Plugin
     */
    void insert( JasperReport jasperReport, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param jasperReport
     *            the reference of the JasperReport
     * @param plugin
     *            the Plugin
     */
    void store( JasperReport jasperReport, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nIdJasperReport
     *            int identifier of the JasperReport to delete
     * @param plugin
     *            the Plugin
     */
    void delete( int nIdJasperReport, Plugin plugin );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strId
     *            The identifier of the jasperReport
     * @param plugin
     *            the Plugin
     * @return The instance of the jasperReport
     */
    JasperReport load( int nKey, Plugin plugin );

    /**
     * Load the data of all the jasperReport objects and returns them as a collection
     * 
     * @param plugin
     *            the Plugin
     * @return The collection which contains the data of all the jasperReport objects
     */
    Collection<JasperReport> selectJasperReportsList( Plugin plugin );

    /**
     *
     * @param strKey
     * @param plugin
     * @return
     */
    JasperReport loadByCode( String strKey, Plugin plugin );
}
