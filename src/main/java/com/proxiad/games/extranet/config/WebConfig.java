package com.proxiad.games.extranet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Value("${logging.commonsRequestLoggingFilter.includeQueryString:false}")
	private String includeQueryString;
	@Value("${logging.commonsRequestLoggingFilter.includeHeaders:false}")
	private String includeHeaders;
	@Value("${logging.commonsRequestLoggingFilter.includePayload:false}")
	private String includePayload;
	@Value("${logging.commonsRequestLoggingFilter.includeClientInfo:false}")
	private String includeClientInfo;
	@Value("${logging.commonsRequestLoggingFilter.maxPayloadLength:10000}")
	private String maxPayloadLength;
	@Value("${logging.commonsRequestLoggingFilter.beforeMessagePrefix:}")
	private String beforeMessagePrefix;
	@Value("${logging.commonsRequestLoggingFilter.afterMessagePrefix:}")
	private String afterMessagePrefix;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins("*");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/**")
				.addResourceLocations("/resources/");
	}

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(Boolean.valueOf(includeQueryString));
        filter.setIncludePayload(Boolean.valueOf(includePayload));
        filter.setMaxPayloadLength(Integer.valueOf(maxPayloadLength));
        filter.setIncludeHeaders(Boolean.valueOf(includeHeaders));
        filter.setIncludeClientInfo(Boolean.valueOf(includeClientInfo));
        if (!StringUtils.isEmpty(beforeMessagePrefix)) {
            filter.setBeforeMessagePrefix(beforeMessagePrefix);
        }
        if (!StringUtils.isEmpty(afterMessagePrefix)) {
            filter.setAfterMessagePrefix(afterMessagePrefix);
        }
        return filter;
    }

}
