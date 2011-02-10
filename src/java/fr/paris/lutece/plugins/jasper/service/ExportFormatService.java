package fr.paris.lutece.plugins.jasper.service;


import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Service fetching the declared section
 */
@SuppressWarnings( "unchecked" )
public enum ExportFormatService
{INSTANCE;
private static final String CORE = "core";
private static Map<String,ApplicationContext> _mapContext = new HashMap<String,ApplicationContext>(  );
private static final String PATH_CONF = "/WEB-INF/conf/";
private static final String DIR_PLUGINS = "plugins/";
private static final String SUFFIX_CONTEXT_FILE = "_context.xml";

    


    
    /**
     * Fetched the subclasses of the section
     * @param strContextName The context name
     * @param classDef The class definition
     * @return A map containing the beans
     */
    public static Map getListSubClasses( String strContextName, Class classDef )
    {
    	
    	ApplicationContext context = getContext( strContextName );

        return context.getBeansOfType( classDef );
    }
    
    /**
     * Gets a Spring Application context from a given name
     * @param strContextName The context's name
     * @return The context
     */
    private static ApplicationContext getContext( String strContextName )
    {
        // Try to get the context from the cache
        ApplicationContext context = (ApplicationContext) _mapContext.get( strContextName );

        if ( context == null )
        {
            // If not found then load the context from the XML file
            String strContextFilePath = AppPathService.getAbsolutePathFromRelativePath( PATH_CONF );

            if ( !strContextName.equals( CORE ) )
            {
                strContextFilePath += DIR_PLUGINS;
            }

            String strContextFile = strContextFilePath + strContextName + SUFFIX_CONTEXT_FILE;

            try
            {
                if ( strContextName.equals( CORE ) )
                {
                    context = new FileSystemXmlApplicationContext( "file:" + strContextFile );
                }
                else
                {
                    List<String> listModules = new ArrayList<String>(  );
                    File dir = new File( strContextFilePath );
                    String[] arrayFiles = dir.list(  );

                    if ( arrayFiles != null )
                    {
                        for ( int i = 0; i < arrayFiles.length; i++ )
                        {
                            String strPath = arrayFiles[i];

                            if ( strPath.startsWith( strContextName ) && strPath.endsWith( "_context.xml" ) )
                            {
                                String strFullPath = "file:" + strContextFilePath + strPath;
                                listModules.add( strFullPath );
                            }
                        }
                    }

                    String[] arrayModules = new String[listModules.size(  )];
                    listModules.toArray( arrayModules );
                    context = new FileSystemXmlApplicationContext( arrayModules );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error retrieving context file : " + e.getMessage(  ), e );
            }
            finally
            {
                _mapContext.put( strContextName, context );
            }

            _mapContext.put( strContextName, context );
        }

        return context;
    }
    
    public Map<String,ILinkJasperReport> getExportTypes( )
    {
    	
    	return getListSubClasses("jasper", ILinkJasperReport.class);
    }
   
}
