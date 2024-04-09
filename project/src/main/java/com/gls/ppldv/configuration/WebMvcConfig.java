package com.gls.ppldv.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import com.gls.ppldv.configuration.security.interceptor.CustomInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private CustomInterceptor customInterceptor;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.setCacheSeconds(0);
		
		// "/resources/img/**" 패턴에 대해서만 캐시 저장 시간을 86400으로 설정
        interceptor.addCacheMapping(CacheControl.maxAge(86400, TimeUnit.SECONDS), "/resources/img/**");

        registry.addInterceptor(interceptor).addPathPatterns("/**");
        
        registry.addInterceptor(customInterceptor)
        		.addPathPatterns("/developer/**")
        		.addPathPatterns("/business/**")
        		.excludePathPatterns("/developer/search")
        		.excludePathPatterns("/developer/searchFirst")
        		.excludePathPatterns("/developer/readOtherPage")
        		.excludePathPatterns("/developer/readViewCount")
        		.excludePathPatterns("/developer/Info")
        		.excludePathPatterns("/business/search")
        		.excludePathPatterns("/business/project");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**") // CORS를 적용할 패턴
				.allowedOrigins("*") // 모든 IP에 응답을 허용하겠다.
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 모든 메소드에 응답을 허용하겠다.
				.allowedHeaders("*") // 모든 헤더에 응답을 허용하겠다.
				.allowCredentials(true) // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
				.maxAge(3600); // pre-flight 요청 결과를 캐시할 시간
	}
	
	
	
	
	
}
