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

public class JasperFileLinkServiceTest extends LuteceTestCase {
    @Test
    public void testExportFile() throws IOException {

        JasperReport reportJRBeanCollection= new JasperReport();
        
        reportJRBeanCollection.setIdReport(1);
        
        
        Properties propTest=getTestProperties( );
        
    
        
        
        reportJRBeanCollection.setUrl(propTest.getProperty( "jasper.reportTestJRBeanUrl"));
        reportJRBeanCollection.setCode(propTest.getProperty("jasper.reportTestJRBeanCode"));
        reportJRBeanCollection.setDateModification( new java.sql.Timestamp(new Date().getTime()));
        
        
        String reportType=propTest.getProperty( "jasper.reportTestGenerateType");
        
        List<TestJavaDataSource> listTestJavaData=new ArrayList<>();
       
        listTestJavaData.add(new TestJavaDataSource());
        HashMap<String, Object> prop=new HashMap<>( );

        
        byte[] testExport=JasperFileLinkService.INSTANCE.exportFile(reportJRBeanCollection, reportType, new JRBeanCollectionDataSource( listTestJavaData ), new HashMap<String, Object>(), null);
    
        
        
        try{
            File ff=new File(propTest.getProperty( "jasper.reportGenerateFilePath")); // définir l'arborescence
            FileOutputStream sortie = new FileOutputStream(ff);
            sortie.write(testExport);
            sortie.close();
            
          } catch (Exception e) {}
            
    }
    
    
    
    private  Properties getTestProperties( )
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
