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
        this.strTitle= "test data source";   
        this.listGroupData = new ArrayList<>( );

        for ( int i = 0; i < 10; i++ )
        {
            GroupData groupData = new GroupData( );
            groupData.setGroupName( "Group Name " + i );
            groupData.setListEntries( new ArrayList<EntryData>( ) );

            for ( int j = 0; j < 10; j++ )
            {
               EntryData entry=new EntryData( );
               entry.setName( "********* ******** entry name ******* ************ **** ****** ******* ************ **** ************* ************ **** ***** ******** ************ **** ****** " +j );
               entry.setValue("********* ******** entry value ************ **** ************* ************ **** ************* ************ **** ************* ************ **** ******  " +j );
               
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
