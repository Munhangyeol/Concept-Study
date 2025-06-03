package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration.class
})
public class ConceptApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConceptApplication.class,args);
	}

}
