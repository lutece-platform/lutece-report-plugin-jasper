package fr.paris.lutece.plugins.jasper.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import fr.paris.lutece.plugins.jasper.business.JasperReport;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperFileLinkServiceTest extends LuteceTestCase {
	@Test
	public void testExportFile() throws IOException {

		JasperReport report= new JasperReport();
		
		report.setIdReport(1);
		report.setFileFolder(AppPathService.getWebAppPath(  )+"/test/files/jasper/hellojasper");
		System.out.println(AppPathService.getWebAppPath(  ));
		
		report.setUrl("test/test.jasper");
		report.setCode("test");
		report.setDateModification( new java.sql.Timestamp(new Date().getTime()));
		
		
		String reportType="pdf";
		
		List<TestJavaDataSource> listTestJavaData=new ArrayList<>();
		listTestJavaData.add(new TestJavaDataSource("test"));
		byte[] testExport=JasperFileLinkService.INSTANCE.exportFile(report, reportType, new JRBeanCollectionDataSource( listTestJavaData ), new HashMap<String, Object>(), null);
	
		
		
		try{
		    File ff=new File("/tmp/jasper/test.pdf"); // définir l'arborescence
		    FileOutputStream sortie = new FileOutputStream(ff);
		    sortie.write(testExport);
		    sortie.close();
		    
		  } catch (Exception e) {}
		    
	}

}
