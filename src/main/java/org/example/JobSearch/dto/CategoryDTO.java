package org.example.JobSearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
}
