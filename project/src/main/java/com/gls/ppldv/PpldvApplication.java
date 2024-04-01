package com.gls.ppldv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.gls.ppldv.*.mapper")
public class PpldvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpldvApplication.class, args);
	}

}
