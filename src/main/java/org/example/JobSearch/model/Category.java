package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Category {
    private Long id;

    private String name;

    private Long parentId;
}
