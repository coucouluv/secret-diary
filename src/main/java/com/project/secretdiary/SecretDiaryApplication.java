package com.project.secretdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SecretDiaryApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties"
            + ",/home/ec2-user/config/real-application.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(SecretDiaryApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}