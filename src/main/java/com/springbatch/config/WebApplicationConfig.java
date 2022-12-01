package com.springbatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebApplicationConfig implements WebMvcConfigurer {


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/jsp/", ".jsp");
    }

    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/_resource_/**").addResourceLocations("classpath:/static/sample/_web_/_resource_/");
        registry.addResourceHandler("/frame/**").addResourceLocations("classpath:/static/sample/_web_/frame/");
        registry.addResourceHandler("/nexacro17lib/**").addResourceLocations("classpath:/static/sample/_web_/nexacro17lib/");
        registry.addResourceHandler("/*.json").addResourceLocations("classpath:/static/sample/");
        registry.addResourceHandler("/*.html").addResourceLocations("classpath:/static/sample/");
        registry.addResourceHandler("/*.js").addResourceLocations("classpath:/static/sample/");
    }


//    @Bean
//    public StringHttpMessageConverter StringHttpMessageConverter() {
//        final StringHttpMessageConverter stringHttpMessageConverter =
//                new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
//        return stringHttpMessageConverter;
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(StringHttpMessageConverter());
//    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        long timeout = 5 * 60 * 1000;
        WebMvcConfigurer.super.configureAsyncSupport(configurer);
        configurer.setDefaultTimeout(timeout);
    }

}
