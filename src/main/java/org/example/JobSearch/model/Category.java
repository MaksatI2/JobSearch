package org.example.JobSearch.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Category {
    private Long id;
    private String name;
    private Long parentId;
}
