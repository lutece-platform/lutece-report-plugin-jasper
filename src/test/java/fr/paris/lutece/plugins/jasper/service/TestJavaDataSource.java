package fr.paris.lutece.plugins.jasper.service;

public class TestJavaDataSource {

    

	private String strDataName;

	public 	TestJavaDataSource(String strDataName)
	{
		
		
		this.strDataName=strDataName;
		
	}

	public String getDataName() {
		return strDataName;
	}

	public void setDataName(String strDataName) {
		this.strDataName = strDataName;
	}
}
