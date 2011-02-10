package fr.paris.lutece.plugins.jasper.service.purge;


import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.File;
/**
 *
 * ImagePurgeService
 *
 */
public class ImagePurgeService 
{
	private static final String PROPERTY_IMAGES_FILES_PATH = "jasper.images.path";

    private ImagePurgeService(  )
    {
    }

    public static void purgeFiles(  )
    {
    	String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_IMAGES_FILES_PATH );
    	String strRootPath = AppPathService.getWebAppPath(  ) + strDirectoryPath; 
    	 File folder = new File( strRootPath );
    	deleteFolderWithContent(folder);
    	File newFolder = new File(strRootPath);
    	newFolder.mkdir();
    }
    public static boolean deleteFolderWithContent( File folder )
    {
        if ( folder.isDirectory(  ) )
        {
            String[] files = folder.list(  );

            for ( int i = 0; i < files.length; i++ )
            {
                boolean success = deleteFolderWithContent( new File( folder, files[i] ) );

                if ( !success )
                {
                    return false;
                }
            }
        }

        return folder.delete(  );
    }
}

