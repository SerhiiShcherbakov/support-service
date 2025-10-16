package com.serhiishcherbakov.supportservice;

import com.serhiishcherbakov.support.SupportServiceApplication;
import org.springframework.boot.SpringApplication;

public class TestSupportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(SupportServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
