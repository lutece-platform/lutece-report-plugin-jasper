package fr.paris.lutece.plugins.jasper.service.purge;

import fr.paris.lutece.portal.service.daemon.Daemon;

public class DaemonPurgeJasperImage extends Daemon{
	 public void run(  )
	    {
		 ImagePurgeService.purgeFiles(  );
	    }

}
