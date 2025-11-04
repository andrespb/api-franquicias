package com.api.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FranquiciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranquiciasApplication.class, args);
    }

}
