package com.springbatch.config;

import com.nexacro.uiadapter17.spring.core.context.ApplicationContextProvider;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroHandlerMethodReturnValueHandler;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroMappingExceptionResolver;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroMethodArgumentResolver;
import com.nexacro.uiadapter17.spring.core.resolve.NexacroRequestMappingHandlerAdapter;
import com.nexacro.uiadapter17.spring.core.view.NexacroFileView;
import com.nexacro.uiadapter17.spring.core.view.NexacroView;
import com.nexacro.uiadapter17.spring.dao.DbVendorsProvider;
import com.nexacro.uiadapter17.spring.dao.Dbms;
import com.nexacro.uiadapter17.spring.dao.dbms.Hsql;
import com.nexacro17.xapi.tx.PlatformType;
import com.nexacro17.xeni.services.GridExportImportServlet;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class NexacroConfig extends WebApplicationConfig implements WebMvcRegistrations {

    @Bean
    @Lazy(false)
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    /**
     * ????????????????????? RequestMappingHandlerAdapter ????????? ??????
     */
    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new NexacroRequestMappingHandlerAdapter();
    }

    /**
     * ????????????????????? ArgumentResolver ??????
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        NexacroMethodArgumentResolver nexacroResolver = new NexacroMethodArgumentResolver();
        resolvers.add(nexacroResolver);

        super.addArgumentResolvers(resolvers);
    }

    /**
     * ????????????????????? ReturnValueHandler ??????
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

        NexacroHandlerMethodReturnValueHandler returnValueHandler = new NexacroHandlerMethodReturnValueHandler();

        NexacroFileView nexacroFileView = new NexacroFileView();
        NexacroView nexacroView     = new NexacroView();
        nexacroView.setDefaultContentType(PlatformType.CONTENT_TYPE_XML);
        nexacroView.setDefaultCharset("UTF-8");

        returnValueHandler.setView(nexacroView);
        returnValueHandler.setFileView(nexacroFileView);

        handlers.add(returnValueHandler);

        super.addReturnValueHandlers(handlers);
    }

    /**
     * ????????????????????? ?????? ?????? ExceptionResolver ??????
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

        NexacroView nexacroView = new NexacroView();
        nexacroView.setDefaultContentType(PlatformType.CONTENT_TYPE_XML);
        nexacroView.setDefaultCharset("UTF-8");

        NexacroMappingExceptionResolver nexacroException = new NexacroMappingExceptionResolver();

        nexacroException.setView(nexacroView);
        nexacroException.setShouldLogStackTrace(true);
        nexacroException.setShouldSendStackTrace(true);
        nexacroException.setDefaultErrorMsg("fail.common.msg");
//        nexacroException.setMessageSource(messageSource());
        nexacroException.setOrder(1);
        resolvers.add(nexacroException);

        super.configureHandlerExceptionResolvers(resolvers);
    }

    /**
     * Dbms??? meta?????? ????????? ?????? bean ??????
     * mybatis ?????? ??? ???????????? ?????? ?????? null ??????
     * nexacro platform ???????????? ????????? ?????? metadata ?????? ?????? ??? ?????? ?????? ??????
     * @return
     */
    @Bean
    public DbVendorsProvider dbmsProvider() {
        DbVendorsProvider dbmsProvider = new DbVendorsProvider();

        Map<String, Dbms> dbvendors = new HashMap<>();
        dbvendors.put("HSQL Database Engine",new Hsql());
        dbmsProvider.setDbvendors(dbvendors);
        return dbmsProvider;
    }

    /**
     * ???????????? ????????? ??????
     */
    @Bean
    public ServletRegistrationBean<GridExportImportServlet> gridExportImportServletBean() {
        ServletRegistrationBean<GridExportImportServlet> bean =
                new ServletRegistrationBean<GridExportImportServlet>( new GridExportImportServlet()
                        , "/XExportImport.do"
                );
        bean.setLoadOnStartup(1);
        return bean;
    }

    /**
     * Register XENI INIT-PARAMETER
     */
    @Bean
    public ServletContextInitializer initializer() {

        return new ServletContextInitializer() {
            /*
             * ????????????????????? ?????? ?????? ??????:nexacro17-xeni.jar??? ????????? ?????? ?????? ??????
             */
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setInitParameter("export-path"       , "file:///C:/Temp/export/");  //?????? export ???????????? ?????? ?????? ????????????
                servletContext.setInitParameter("import-path"       , "file:///C:/Temp/import/");  //?????? import ???????????? ?????? ?????? ????????????
                servletContext.setInitParameter("monitor-enabled"   , "true");    //???????????? ????????? ?????? ???????????? ??????
                servletContext.setInitParameter("monitor-cycle-time", "30");      //???????????? ????????? ?????? ???????????? ??????( default:???)
                servletContext.setInitParameter("file-storage-time" , "10");      //???????????? ?????? ???????????? ???????????? ?????? (default:???)
            }
        };
    }

//    @Bean
//    public MessageSource messageSource() {
//        var messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

    /**
     * CommonsMultipartResolver ??????
     * Multipart Resolver - Upload upto 10 Megabytes
     * This is for XENI IMPORT
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSize(100000000);
        resolver.setMaxInMemorySize(100000000);
        resolver.setUploadTempDir(tempFileDir());
        return resolver;
    }

    /**
     * ?????? ????????? Temp ?????? ??????
     * windows ????????? ?????? ?????? ??????
     */
    @Bean
    public Resource tempFileDir() {
        Resource resource = new FileSystemResource("C:\\Temp");
        return resource;
    }

    /**
     *  MultipartFilter ?????????
     */
    @Bean
    @Order(0)
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }

}
