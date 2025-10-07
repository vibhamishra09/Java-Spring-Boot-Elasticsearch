package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest
@Testcontainers
public class ProductServiceIntegrationTest {

    @Container
    private static final ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.19.4"));

    @DynamicPropertySource
    static void overrideElasticsearchProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.elasticsearch.rest.client.reactive.endpoints", 
                     () -> elasticsearchContainer.getHttpHostAddress());
        registry.add("spring.data.elasticsearch.client.reactive.max-connection", 
                     () -> "1000");
        registry.add("spring.data.elasticsearch.client.reactive.connection-timeout", 
                     () -> "10000");
        registry.add("spring.data.elasticsearch.client.reactive.socket-timeout", 
                     () -> "10000");
    }

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

   
@Test
void testSearchWithFilters() {
    // Create and save products
    Product product1 = new Product(
        "1", 
        "Introduction to Mathematics", 
        "A beginner's course to understand the basics of mathematics, including algebra, geometry, and number theory.", 
        "Math", 
        "COURSE", 
        "1st–3rd", 
        6, 
        9, 
        45.99, 
        "2025-06-01T10:00:00Z"
    );

    Product product2 = new Product(
        "2", 
        "Science Fundamentals", 
        "An introductory course for young learners to explore basic scientific concepts such as gravity, force, and simple machines.", 
        "Science", 
        "COURSE", 
        "1st–3rd", 
        6, 
        9, 
        50.00, 
        "2025-06-02T10:00:00Z"
    );

    Product product3 = new Product(
        "3", 
        "Art and Creativity", 
        "A fun-filled art course where kids can learn painting, drawing, and color theory.", 
        "Art", 
        "COURSE", 
        "4th–6th", 
        9, 
        12, 
        60.50, 
        "2025-06-03T14:00:00Z"
    );

    elasticsearchOperations.save(product1);
    elasticsearchOperations.save(product2);
    elasticsearchOperations.save(product3);

    // Create filter criteria for price and category (with flexible criteria for 'Math' related products)
    Criteria criteria = new Criteria("price").greaterThanEqual(40.0)
            .and(new Criteria("title").matches("Mathematics")); // Only search for courses related to "Mathematics"
    
    // Apply sorting by price ascending
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("price")));
    CriteriaQuery query = new CriteriaQuery(criteria, pageable);

    // Perform the search
    SearchHits<Product> hits = elasticsearchOperations.search(query, Product.class);
    List<Product> products = hits.stream().map(SearchHit::getContent).toList();

    // Assert that two products are found
    assertThat(products).hasSize(1);

    // Assert that the products are sorted by price ascending
    assertThat(products.get(0).getTitle()).isEqualTo("Introduction to Mathematics");
    
}

}
