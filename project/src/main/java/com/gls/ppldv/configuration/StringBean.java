package com.gls.ppldv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:prop/file.properties")
public class StringBean {
	
	@Value("${upload.file}")
	private String realPath;
	
	@Bean
	public String realPath() {
		return realPath;
	}
}
