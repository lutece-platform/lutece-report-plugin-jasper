package fr.paris.lutece.plugins.jasper.service;

import java.util.List;

public class GroupData
{
    private String _strGroupName;
    private List<EntryData> listEntries;

    public String getGroupName( )
    {
        return _strGroupName;
    }

    public void setGroupName( String _strGroupName )
    {
        this._strGroupName = _strGroupName;
    }

    public List<EntryData> getListEntries( )
    {
        return listEntries;
    }

    public void setListEntries( List<EntryData> listEntries )
    {
        this.listEntries = listEntries;
    }
}
