package com.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.entity.Product;

public interface Productrepo extends ElasticsearchRepository<Product, String> {

}
