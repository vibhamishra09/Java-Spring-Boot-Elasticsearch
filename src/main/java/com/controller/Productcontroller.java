package com.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Productservice;
import com.entity.Product;
import com.entity.SearchResponse;
import com.entity.SearchRequest;

@RestController
@RequestMapping("/api")
public class Productcontroller {
    @Autowired
    private Productservice productservice;
    @GetMapping("/search")
    public SearchResponse searchCourses(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "minAge", required = false) Integer minAge,
        @RequestParam(value = "maxAge", required = false) Integer maxAge,
        @RequestParam(value = "minPrice", required = false) Double minPrice,
        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "sort", defaultValue = "upcoming") String sort,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
) {
    // Create a search request object and pass it to the service
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setKeyword(keyword);
    searchRequest.setMinAge(minAge);
    searchRequest.setMaxAge(maxAge);
    searchRequest.setMinPrice(minPrice);
    searchRequest.setMaxPrice(maxPrice);
    searchRequest.setCategory(category);
    searchRequest.setType(type);
    searchRequest.setStartDate(startDate);
    searchRequest.setSort(sort);
    searchRequest.setPage(page);
    searchRequest.setSize(size);
    List<Product> courses = productservice.searchCourses(searchRequest);

    // Calculate the total number of courses returned
    long totalHits = courses.size();

    // Return the result as a response
    SearchResponse response = new SearchResponse();
    response.setTotal(totalHits);
    response.setProducts(courses);
    return response;
}
}

