package com.n26.coding.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan({ "com.n26.coding.challenge" })
public class ChallengeWebApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ChallengeWebApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ChallengeWebApplication.class, args);
		System.out.println("Started...");
	}
    
	/**
	 * This method is used to configure swagger for REST endpoints.
	 * 
	 * @return
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.n26.coding.challenge.controllers")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Coding Challenge API Gateway", "Coding Challenge API Gateway.", "1.0", "", "", "Apache 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0");
		return apiInfo;
	}

}
