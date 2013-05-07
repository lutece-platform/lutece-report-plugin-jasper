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
package fr.paris.lutece.plugins.jasper.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for JasperReport objects
 */
public final class JasperReportDAO implements IJasperReportDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_report ) FROM jasper";
    private static final String SQL_QUERY_SELECT = "SELECT id_report, description, url,pool,file_folder FROM jasper WHERE id_report = ?";
    private static final String SQL_QUERY_SELECT_BY_DESC = "SELECT id_report, description, url, pool FROM jasper WHERE description = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO jasper ( id_report, description , url ,pool) VALUES ( ?, ? , ?,?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM jasper WHERE id_report = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE jasper SET id_report = ?, description = ? WHERE id_report = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_report, description, url,pool FROM jasper";
    private static final String SQL_QUERY_SELECT_FILE_FORMATS = "SELECT  file_format FROM jasper_file_format WHERE  id_report= ? ";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     * @param report instance of the JasperReport object to insert
     * @param plugin The plugin
     */
    public void insert( JasperReport report, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        report.setIdReport( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, report.getIdReport(  ) );
        daoUtil.setString( 2, report.getDescription(  ) );
        daoUtil.setString( 3, report.getUrl(  ) );
        daoUtil.setString( 4, report.getPool(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the report from the table
     * @param nId The identifier of the report
     * @param plugin The plugin
     * @return the instance of the JasperReport
     */
    public JasperReport load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        JasperReport report = null;

        if ( daoUtil.next(  ) )
        {
            report = new JasperReport(  );

            report.setIdReport( daoUtil.getInt( 1 ) );
            report.setDescription( daoUtil.getString( 2 ) );
            report.setUrl( daoUtil.getString( 3 ) );
            report.setPool( daoUtil.getString( 4 ) );
            report.setFileFolder( daoUtil.getString( 5 ) );
            report.addFileFormats( loadFileFormats( nId, plugin ) );
        }

        daoUtil.free(  );

        return report;
    }

    /**
     * Load the file formats
     * @param nId The identifier of the report
     * @param plugin The plugin
     * @return the list of file formats attached to the report
     */
    public ArrayList<String> loadFileFormats( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FILE_FORMATS, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        ArrayList<String> listFileFormats = new ArrayList<String>(  );

        while ( daoUtil.next(  ) )
        {
            listFileFormats.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return listFileFormats;
    }

    /**
     * Delete a record from the table
     * @param nJasperReportId The identifier of the report
     * @param plugin The plugin
     */
    public void delete( int nJasperReportId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nJasperReportId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param report The reference of the report
     * @param plugin The plugin
     */
    public void store( JasperReport report, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, report.getIdReport(  ) );
        daoUtil.setString( 2, report.getDescription(  ) );
        daoUtil.setInt( 3, report.getIdReport(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the reports and returns them as a collection
     * @param plugin The plugin
     * @return The Collection which contains the data of all the reports
     */
    public Collection<JasperReport> selectJasperReportsList( Plugin plugin )
    {
        Collection<JasperReport> reportList = new ArrayList<JasperReport>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            JasperReport report = new JasperReport(  );

            report.setIdReport( daoUtil.getInt( 1 ) );
            report.setDescription( daoUtil.getString( 2 ) );
            report.setUrl( daoUtil.getString( 3 ) );
            report.setPool( daoUtil.getString( 4 ) );

            reportList.add( report );
        }

        daoUtil.free(  );

        return reportList;
    }

    public JasperReport load( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DESC, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery(  );

        JasperReport report = null;

        if ( daoUtil.next(  ) )
        {
            report = new JasperReport(  );

            report.setIdReport( daoUtil.getInt( 1 ) );
            report.setDescription( daoUtil.getString( 2 ) );
            report.setUrl( daoUtil.getString( 3 ) );
            report.setPool( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return report;
    }
}
