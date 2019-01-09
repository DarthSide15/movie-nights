package com.darthside.movienights;
import com.darthside.movienights.database.MovieTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // run() is used to launch the Application
        SpringApplication.run(Application.class, args);


    }
}
