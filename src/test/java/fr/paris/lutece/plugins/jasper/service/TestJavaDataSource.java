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
import java.util.List;

public class TestJavaDataSource
{

    private String strNumber;
    private String strTitle;

    private List<GroupData> listGroupData;

    public TestJavaDataSource( )
    {
        this.strNumber = "101";
        this.strTitle = "test data source";
        this.listGroupData = new ArrayList<>( );

        for ( int i = 0; i < 10; i++ )
        {
            GroupData groupData = new GroupData( );
            groupData.setGroupName( "Group Name " + i );
            groupData.setListEntries( new ArrayList<EntryData>( ) );

            for ( int j = 0; j < 10; j++ )
            {
                EntryData entry = new EntryData( );
                entry.setName(
                        "********* ******** entry name ******* ************ **** ****** ******* ************ **** ************* ************ **** ***** ******** ************ **** ****** "
                                + j );
                entry.setValue(
                        "********* ******** entry value ************ **** ************* ************ **** ************* ************ **** ************* ************ **** ******  "
                                + j );

                groupData.getListEntries( ).add( entry );

            }

            EntryData entry = new EntryData( );
            entry.setName( "" );
            listGroupData.add( groupData );
        }

    }

    public String getNumber( )
    {
        return strNumber;
    }

    public void setNumber( String strNumber )
    {
        this.strNumber = strNumber;
    }

    public List<GroupData> getListGroupData( )
    {
        return listGroupData;
    }

    public void setListGroupData( List<GroupData> listGroupData )
    {
        this.listGroupData = listGroupData;
    }

    public String getTitle( )
    {
        return strTitle;
    }

    public void setTitle( String strTitle )
    {
        this.strTitle = strTitle;
    }

}
