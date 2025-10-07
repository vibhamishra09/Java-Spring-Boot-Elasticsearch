package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final Productservice productservice;

    @Autowired
    public DataLoader(Productservice productservice) {
        this.productservice = productservice;
    }

    @Override
    public void run(String... args) throws Exception {
        productservice.bulkIndexCourses();
    }
}


