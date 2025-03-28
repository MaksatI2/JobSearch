package org.example.JobSearch.exceptions;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseBody {
    private String title;
    private String detail;
    private Map<String, List> reasons;
    private String timestamp;
}
