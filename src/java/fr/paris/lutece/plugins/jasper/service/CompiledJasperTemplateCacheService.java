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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 * The Class CompiledJasperTemplateCacheService.
 */
public class CompiledJasperTemplateCacheService extends AbstractCacheableService
{

    /** The Constant SERVICE_NAME. */
    private static final String SERVICE_NAME = "CompiledJasperTemplateCacheService";

    /**
     * Instantiates a new compiled jasper template cache service.
     */
    public CompiledJasperTemplateCacheService( )
    {
        initCache( );
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /** The compiled jasper template cache service. */
    private static CompiledJasperTemplateCacheService _compiledJasperTemplateCacheService;

    /**
     * Gets the single instance of CompiledJasperTemplateCacheService.
     *
     * @return single instance of CompiledJasperTemplateCacheService
     */
    public static CompiledJasperTemplateCacheService getInstance( )
    {
        if ( _compiledJasperTemplateCacheService == null )
        {
            _compiledJasperTemplateCacheService = new CompiledJasperTemplateCacheService( );
        }
        return _compiledJasperTemplateCacheService;
    }

    /**
     * Gets the compiled jasper template.
     *
     * @param code
     *            the code
     * @param reportFile
     *            the report file
     * @return the compiled jasper template
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws JRException
     *             the JR exception
     */
    public JasperReport getCompiledJasperTemplate( String code, File reportFile ) throws FileNotFoundException, JRException
    {
        String cacheKey = getCacheKey( code );
        JasperReport r = ( JasperReport ) getFromCache( cacheKey );
        if ( r == null )
        {
            r = JasperCompileManager.compileReport( new FileInputStream( reportFile ) );
            putInCache( cacheKey, r );
        }
        return r;
    }

    /**
     * Gets the cache key.
     *
     * @param type
     *            the type
     * @return the cache key
     */
    private String getCacheKey( String type )
    {
        StringBuilder sbKey = new StringBuilder( );
        sbKey.append( "[code:" ).append( type ).append( "]" );
        return sbKey.toString( );
    }

}
