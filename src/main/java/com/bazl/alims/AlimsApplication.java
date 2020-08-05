package com.bazl.alims;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.bazl.alims.dao")//不加会报NoSuchBeanDefinitionException
public class AlimsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlimsApplication.class, args);
	}

}

