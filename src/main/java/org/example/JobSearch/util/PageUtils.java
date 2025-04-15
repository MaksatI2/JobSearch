package org.example.JobSearch.util;

import org.example.JobSearch.dto.page.Page;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PageUtils {
    public <T> Page<T> createPage(List<T> content, int page, int size, int totalElements) {
        Page<T> result = new Page<>();
        result.setContent(content);
        result.setPage(page);
        result.setSize(size);
        result.setTotalElements(totalElements);
        result.setTotalPages((int) Math.ceil((double) totalElements / size));
        return result;
    }
}
