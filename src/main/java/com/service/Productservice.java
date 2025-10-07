package com.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.entity.Product;
import com.entity.SearchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repo.Productrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

@Service
public class Productservice {
    @Autowired
    private Productrepo productrepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public void bulkIndexCourses() throws IOException {
        // Read the products JSON file from resources folder
        File file = new File(getClass().getClassLoader().getResource("sample_courses.json").getFile());

        // Deserialize JSON file into a list of Product objects
        List<Product> products = objectMapper.readValue(file, new TypeReference<List<Product>>() {});

        // Bulk index the data into Elasticsearch
        productrepo.saveAll(products);
    }
    public List<Product> searchCourses(SearchRequest searchRequest) {
        // Build Criteria
        Criteria criteria = new Criteria();

        // Full-text search on title OR description
        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            Criteria keywordCriteria = new Criteria("title").matches(searchRequest.getKeyword())
                    .or(new Criteria("description").matches(searchRequest.getKeyword()));
            criteria = criteria.and(keywordCriteria);
        }

        // Range filters for minAge and maxAge
        if (searchRequest.getMinAge() != null) {
            criteria = criteria.and(new Criteria("minAge").greaterThanEqual(searchRequest.getMinAge()));
        }
        if (searchRequest.getMaxAge() != null) {
            criteria = criteria.and(new Criteria("maxAge").lessThanEqual(searchRequest.getMaxAge()));
        }

        // Range filters for price
        if (searchRequest.getMinPrice() != null) {
            criteria = criteria.and(new Criteria("price").greaterThanEqual(searchRequest.getMinPrice()));
        }
        if (searchRequest.getMaxPrice() != null) {
            criteria = criteria.and(new Criteria("price").lessThanEqual(searchRequest.getMaxPrice()));
        }

        // Exact filters for category and type
        if (searchRequest.getCategory() != null && !searchRequest.getCategory().isEmpty()) {
            criteria = criteria.and(new Criteria("category").is(searchRequest.getCategory()));
        }
        if (searchRequest.getType() != null && !searchRequest.getType().isEmpty()) {
            criteria = criteria.and(new Criteria("type").is(searchRequest.getType()));
        }

        // Date filter for nextSessionDate (assumes stored as ISO-8601 strings)
        if (searchRequest.getStartDate() != null && !searchRequest.getStartDate().isEmpty()) {
            criteria = criteria.and(new Criteria("nextSessionDate").greaterThanEqual(searchRequest.getStartDate()));
        }

        // Sorting logic
        Sort sort = Sort.by(Sort.Order.asc("nextSessionDate"));
        if ("priceAsc".equals(searchRequest.getSort())) {
            sort = Sort.by(Sort.Order.asc("price"));
        } else if ("priceDesc".equals(searchRequest.getSort())) {
            sort = Sort.by(Sort.Order.desc("price"));
        } else if ("upcoming".equals(searchRequest.getSort())) {
            sort = Sort.by(Sort.Order.asc("nextSessionDate"));
        }

        // Pagination
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        // Execute the search query
        CriteriaQuery query = new CriteriaQuery(criteria, pageable);
        SearchHits<Product> hits = elasticsearchOperations.search(query, Product.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }
}
