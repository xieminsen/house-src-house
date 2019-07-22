package com.xms.house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2//swagger2注解生成接口文档
public class HouseEnvApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseEnvApplication.class, args);
	}
}
