package com.blog.mavenserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MavenServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavenServerApplication.class, args);
    }

}
