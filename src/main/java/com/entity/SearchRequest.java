package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String keyword;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private String category;
    private String type;
    private String startDate;  // Use ISO-8601 date format (e.g., "2025-06-10T00:00:00Z")
    private String sort;       // Sorting parameter (priceAsc, priceDesc, upcoming)
    private int page = 0;      // Default page is 0
    private int size = 10;     // Default size is 10


}
