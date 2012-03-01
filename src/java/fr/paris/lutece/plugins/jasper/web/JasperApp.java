/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.jasper.web;

import fr.paris.lutece.plugins.jasper.service.JasperFileLinkService;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides a simple implementation of an XPage
 */
public class JasperApp extends AbstractCacheableService implements XPageApplication
{
    private static final String PROPERTY_PATH_LABEL = "jasper.pagePathLabel";
    private static final String PROPERTY_PAGE_TITLE = "jasper.pageTitle";
    // private static Map<String, String> _mapSiteMapCache = new HashMap<String,
    // String>( );
    private static final String SERVICE_NAME = "JasperService";
    private static boolean _bRegister;

    /**
     * Creates a new JasperApp object
     */
    public JasperApp(  )
    {
        if ( !_bRegister )
        {
            initCache();
            _bRegister = true;
        }
    }

    /**
    * Returns the content of the page jasper.
    * @param request The http request
    * @param nMode The current mode
    * @param plugin The plugin object
    * @throws fr.paris.lutece.portal.service.message.SiteMessageException Message displayed if an exception occurs
    */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws SiteMessageException
    {
        XPage page = new XPage(  );
        // String strKey = JasperFileLinkService.INSTANCE.getKey( request );

        Locale locale = request.getLocale(  );

        // Check the key in the cache
        // if ( !_mapSiteMapCache.containsKey( strKey ) )
        // {
            byte[] arrayContent = JasperFileLinkService.exportFile( request );

            // Build the HTML document from the report
            String strPage = new String( arrayContent );

        // // Add it to the cache
        // _mapSiteMapCache.put( strKey, strPage );

            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATH_LABEL, locale ) );
            page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale ) );
            page.setContent( strPage );

        return page;
        // }

        // The document exist in the cache
        // page.setPathLabel( I18nService.getLocalizedString(
        // PROPERTY_PATH_LABEL, locale ) );
        // page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE,
        // locale ) );
        // page.setContent( (String) _mapSiteMapCache.get( strKey ) );

        // return page;
    }


    public String getName(  )
    {
        return SERVICE_NAME;
    }

}
