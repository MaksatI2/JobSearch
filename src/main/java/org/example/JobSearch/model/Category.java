package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Category {
    private Integer id;

    private String name;

    private Category parentCategory;
}
