package com.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResponse {
    private long total;
    private List<Product> products;

    // Getters and Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Product> getCourses() {
        return products;
    }

    public void setCourses(List<Product> products) {
        this.products = products;
    }
   
}


