package com.darthside.movienights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation adds a few other annotations to this class,
// for example the @ComponentScan annotation which looks for
// components, services and configurations within the package
// allowing it to fins the controllers
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // run() is used to launch the Application
        SpringApplication.run(Application.class, args);
    }

    
}
