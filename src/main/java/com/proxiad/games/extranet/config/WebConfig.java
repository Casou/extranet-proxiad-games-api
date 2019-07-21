package com.proxiad.games.extranet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties
public class WebConfig implements WebMvcConfigurer {

	@Value("${logging.commonsRequestLoggingFilter.includeQueryString:false}")
	private Boolean includeQueryString;
	@Value("${logging.commonsRequestLoggingFilter.includeHeaders:false}")
	private Boolean includeHeaders;
	@Value("${logging.commonsRequestLoggingFilter.includePayload:false}")
	private Boolean includePayload;
	@Value("${logging.commonsRequestLoggingFilter.includeClientInfo:false}")
	private Boolean includeClientInfo;
	@Value("${logging.commonsRequestLoggingFilter.maxPayloadLength:10000}")
	private Integer maxPayloadLength;
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
        filter.setIncludeQueryString(includeQueryString);
        filter.setIncludePayload(includePayload);
        filter.setMaxPayloadLength(maxPayloadLength);
        filter.setIncludeHeaders(includeHeaders);
        filter.setIncludeClientInfo(includeClientInfo);
        if (!StringUtils.isEmpty(beforeMessagePrefix)) {
            filter.setBeforeMessagePrefix(beforeMessagePrefix);
        }
        if (!StringUtils.isEmpty(afterMessagePrefix)) {
            filter.setAfterMessagePrefix(afterMessagePrefix);
        }
        return filter;
    }

}
