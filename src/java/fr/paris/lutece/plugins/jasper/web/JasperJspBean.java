/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.jasper.business.JasperReport;
import fr.paris.lutece.plugins.jasper.business.JasperReportHome;
import fr.paris.lutece.plugins.jasper.service.ExportFormatService;
import fr.paris.lutece.plugins.jasper.service.ILinkJasperReport;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage JasperReport features ( manage, create, modify, remove )
 */
public class JasperJspBean extends PluginAdminPageJspBean
{
    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_JASPER = "JASPER_MANAGEMENT";

    // Parameters
    private static final String PARAMETER_JASPERREPORT_ID_REPORT = "jasperreport_id_report";
    private static final String PARAMETER_JASPERREPORT_CODE = "jasperreport_code";
    private static final String PARAMETER_REPORT_FILE_FOLDER = "file_folder";
    private static final String PARAMETER_REPORT_TEMPLATE = "report_template";
    private static final String PARAMETER_REPORT_POOL = "report_pool";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // templates
    private static final String TEMPLATE_MANAGE_JASPERREPORTS = "/admin/plugins/jasper/manage_jasperreport.html";
    private static final String TEMPLATE_CREATE_JASPERREPORT = "/admin/plugins/jasper/create_jasperreport.html";
    private static final String TEMPLATE_MODIFY_JASPERREPORT = "/admin/plugins/jasper/modify_jasperreport.html";
    private static final String TEMPLATE_MANAGE_REPORT_FILE_TYPES = "/admin/plugins/jasper/manage_report_file_types.html";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_JASPERREPORTS = "jasper.manage_jasperreports.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_JASPERREPORT = "jasper.modify_jasperreport.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_JASPERREPORT = "jasper.create_jasperreport.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_REPORT_FILE_TYPES = "jasper.manage_report_file_types.pageTitle";

    // Markers
    private static final String MARK_JASPERREPORT_LIST = "jasperreport_list";
    private static final String MARK_JASPERREPORT = "jasperreport";
    private static final String MARK_FILE_TYPES = "file_types";
    private static final String MARK_GENERATED_FILE_TYPES = "generated_file_types";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_SECTION_POOL_LIST = "pool_list";

    // Jsp Definition
    private static final String JSP_DO_REMOVE_JASPERREPORT = "jsp/admin/plugins/jasper/DoRemoveJasperReport.jsp";
    private static final String JSP_MANAGE_JASPERREPORTS = "jsp/admin/plugins/jasper/ManageJasperReports.jsp";
    private static final String JSP_REDIRECT_TO_MANAGE_JASPERREPORTS = "ManageJasperReports.jsp";
    private static final String PROPERTY_FILES_PATH = "jasper.files.path";
    protected static final String PROPERTY_FILES_ROOT_PATH = "jasper.files.root.path";

    // Properties
    private static final String PROPERTY_DEFAULT_LIST_JASPERREPORT_PER_PAGE = "jasper.listJasperReports.itemsPerPage";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_JASPERREPORT = "jasper.message.confirmRemoveJasperReport";

    // Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Returns the list of jasperreport
     *
     * @param request
     *            The Http request
     * @return the jasperreports list
     */
    public String getManageJasperReports( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_JASPERREPORTS );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_JASPERREPORT_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_MANAGE_JASPERREPORTS );
        String strUrl = url.getUrl( );
        Collection<JasperReport> listReports = JasperReportHome.getJasperReportsList( getPlugin( ) );
        Paginator paginator = new Paginator( (List<JasperReport>) listReports, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_JASPERREPORT_LIST, paginator.getPageItems( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_JASPERREPORTS, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Returns the form to create a jasperreport
     *
     * @param request
     *            The Http request
     * @return the html code of the jasperreport form
     */
    public String getCreateJasperReport( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_JASPERREPORT );

        Map<String, Object> model = new HashMap<String, Object>( );

        ReferenceList listPools = new ReferenceList( );
        AppConnectionService.getPoolList( listPools );

        model.put( MARK_SECTION_POOL_LIST, listPools );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_JASPERREPORT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form of a new jasperreport
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateJasperReport( HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        JasperReport jasperreport = new JasperReport( );

        if ( request.getParameter( PARAMETER_JASPERREPORT_CODE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        String strReportCode = request.getParameter( PARAMETER_JASPERREPORT_CODE );
        String strPoolName = request.getParameter( PARAMETER_REPORT_POOL );
        String strCleanReportCode = UploadUtil.cleanFileName( strReportCode );
        jasperreport.setCode( strCleanReportCode );
        jasperreport.setPool( strPoolName );

        FileItem fileItem = multipartRequest.getFile( PARAMETER_REPORT_TEMPLATE );

        try
        {
            localTemplateFile( jasperreport, fileItem, strCleanReportCode, true );
        }
        catch( IOException e )
        {
            AppLogService.error( e );
        }

        JasperReportHome.create( jasperreport, getPlugin( ) );

        return JSP_REDIRECT_TO_MANAGE_JASPERREPORTS;
    }

    /**
     * Manages the removal form of a jasperreport whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    public String getConfirmRemoveJasperReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        UrlItem url = new UrlItem( JSP_DO_REMOVE_JASPERREPORT );
        url.addParameter( PARAMETER_JASPERREPORT_ID_REPORT, nId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_JASPERREPORT, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Handles the removal form of a jasperreport
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage jasperreports
     */
    public String doRemoveJasperReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );

        // TODO remove appropriate folder
        JasperReport report = JasperReportHome.findByPrimaryKey( nId, getPlugin( ) );
        String strCleanReportName = UploadUtil.cleanFileName( report.getCode( ) );

        String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
        String strRootFilesPath = AppPropertiesService.getProperty( PROPERTY_FILES_ROOT_PATH );
        String strRootFilesDirectory = StringUtils.isNoneBlank( strRootFilesPath ) ? strRootFilesPath : AppPathService.getWebAppPath( );
        String strFolderPath = strRootFilesDirectory + strDirectoryPath + strCleanReportName;
        File folder = new File( strFolderPath );
        deleteFolderWithContent( folder );
        JasperReportHome.remove( nId, getPlugin( ) );

        return JSP_REDIRECT_TO_MANAGE_JASPERREPORTS;
    }

    /**
     * Returns the form to update info about a jasperreport
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    public String getModifyJasperReport( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_JASPERREPORT );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        JasperReport jasperreport = JasperReportHome.findByPrimaryKey( nId, getPlugin( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_JASPERREPORT, jasperreport );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_JASPERREPORT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the fiule type selection for generation
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    public String getManageFileTypes( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_REPORT_FILE_TYPES );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        JasperReport jasperreport = JasperReportHome.findByPrimaryKey( nId, getPlugin( ) );
        Collection<ILinkJasperReport> listFileTypes = ExportFormatService.INSTANCE.getExportTypes( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_JASPERREPORT, jasperreport );
        model.put( MARK_FILE_TYPES, listFileTypes );
        model.put( MARK_GENERATED_FILE_TYPES, jasperreport.getFileFormats( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_REPORT_FILE_TYPES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the change form of a jasperreport
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyJasperReport( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        JasperReport jasperreport = JasperReportHome.findByPrimaryKey( nId, getPlugin( ) );

        if ( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdReport = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        jasperreport.setIdReport( nIdReport );

        if ( request.getParameter( PARAMETER_JASPERREPORT_CODE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        jasperreport.setCode( request.getParameter( PARAMETER_JASPERREPORT_CODE ) );
        JasperReportHome.update( jasperreport, getPlugin( ) );

        return JSP_REDIRECT_TO_MANAGE_JASPERREPORTS;
    }

    /**
     * Process the change form of a jasperreport
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyReportFileTypes( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        JasperReport jasperreport = JasperReportHome.findByPrimaryKey( nId, getPlugin( ) );

        if ( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdReport = Integer.parseInt( request.getParameter( PARAMETER_JASPERREPORT_ID_REPORT ) );
        jasperreport.setIdReport( nIdReport );

        if ( request.getParameter( PARAMETER_REPORT_FILE_FOLDER ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        jasperreport.setFileFolder( request.getParameter( PARAMETER_REPORT_FILE_FOLDER ) );
        // TODO Important To change file types
        JasperReportHome.update( jasperreport, getPlugin( ) );

        return JSP_REDIRECT_TO_MANAGE_JASPERREPORTS;
    }

    /**
     *
     * @param report
     * @param fileItem
     * @param strReportCode
     * @param bUpdateJasper
     * @throws IOException
     */
    private void localTemplateFile( JasperReport report, FileItem fileItem, String strReportCode, boolean bUpdateJasper ) throws IOException
    {
        String strFileName = fileItem.getName( );
        File file = new File( strFileName );

        if ( !file.getName( ).equals( "" ) && !strReportCode.equals( null ) )
        {
            String strNameFile = file.getName( );

            String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            String strRootFilesPath = AppPropertiesService.getProperty( PROPERTY_FILES_ROOT_PATH );
            String strRootFilesDirectory = StringUtils.isNoneBlank( strRootFilesPath ) ? strRootFilesPath : AppPathService.getWebAppPath( );
            String strFolderPath = strRootFilesDirectory + strDirectoryPath + strReportCode;
            File folder = new File( strFolderPath );

            try
            {
                if ( !folder.exists( ) )
                {
                    folder.mkdir( );
                }
            }
            catch( Exception e )
            {
                AppLogService.error( e );
            }

            String filePath = strRootFilesDirectory + strDirectoryPath + strReportCode + "/" + strNameFile;

            if ( !new File( filePath ).isDirectory( ) && bUpdateJasper )
            {
                file = new File( filePath );

                if ( file.exists( ) )
                {
                    file.delete( );
                }

                FileOutputStream fosFile = new FileOutputStream( file );
                fosFile.flush( );
                fosFile.write( fileItem.get( ) );
                fosFile.close( );
                report.setUrl( strReportCode + "/" + strNameFile );
            }
        }
        else
        {
            report.setUrl( "" );
        }
    }

    public static boolean deleteFolderWithContent( File folder )
    {
        if ( folder.isDirectory( ) )
        {
            String [ ] files = folder.list( );

            for ( int i = 0; i < files.length; i++ )
            {
                boolean success = deleteFolderWithContent( new File( folder, files [i] ) );

                if ( !success )
                {
                    return false;
                }
            }
        }

        return folder.delete( );
    }
}
