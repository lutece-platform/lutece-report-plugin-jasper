/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.util.ArrayList;


/**
 * This is the business class for the object JasperReport
 */
public class JasperReport
{
    // Variables declarations 
    private int _nIdReport;
    private String _strDescription;
    private String _strUrl;
    private String _strPool;
    private String _strFileFolder;
    private java.sql.Timestamp _dateModification;
    private ArrayList<String> _listFileFormats= new ArrayList<String>();

    /**
     * Returns the IdReport
     * @return The IdReport
     */
    public int getIdReport(  )
    {
        return _nIdReport;
    }

    /**
     * Sets the IdReport
     * @param nIdReport The IdReport
     */
    public void setIdReport( int nIdReport )
    {
        _nIdReport = nIdReport;
    }

    /**
     * Returns the Description
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    public String getFileFolder(  )
    {
        return _strFileFolder;
    }

    public void setFileFolder( String strFileFolder )
    {
        _strFileFolder = strFileFolder;
    }


    /**
     * Returns the Url of the jasper file
     * @return The Url of the jasper file
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url of the jasper file
     * @param strUrl The url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    public String getPool(  )
    {
        return _strPool;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */
    public void setPool( String strPool )
    {
        _strPool = strPool;
    }

    /**
     * Returns the Date of the last Modification
     *
     * @return The Date  of the last Modification
     */
    public java.sql.Timestamp getDateModification(  )
    {
        return _dateModification;
    }

    /**
     * Sets the Date  of the last Modification
     *
     * @param dateModification The Date  of the last Modification
     */
    public void setDateModification( java.sql.Timestamp dateModification )
    {
        _dateModification = dateModification;
    }
    /**
     * Adds a file format
     *
     * @param strFileFormat A file format chosen
     */
    public void addFileFormat( String strFileFormat )
    {
        _listFileFormats.add(strFileFormat);
    }
    /**
     * Adds a file format
     *
     * @param listFileFormat A file formats list
     */
    public void addFileFormats( ArrayList<String> listFileFormat )
    {
    	for(String strFileFormat :listFileFormat)
    	{
    		addFileFormat(strFileFormat);
    	}
    }
    public ArrayList<String> getFileFormats(  )
    {
        return _listFileFormats;
    }
}
