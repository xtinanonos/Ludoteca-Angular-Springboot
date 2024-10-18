package com.ccsw.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TutorialApplication {

    public static void main(String[] args) {
        System.out.println("Current TimeZone: " + TimeZone.getDefault().getID());
        var context = SpringApplication.run(TutorialApplication.class, args);
    }

}
