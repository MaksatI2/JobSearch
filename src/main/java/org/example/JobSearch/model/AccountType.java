package org.example.JobSearch.model;

import lombok.Getter;

@Getter
public enum AccountType {
    APPLICANT("APPLICANT", 1L),
    EMPLOYER("EMPLOYER", 2L);

    private final String name;
    private final Long id;

    AccountType(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static AccountType getById(Long id) {
        for (AccountType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Нет AccountType с ID" + id);
    }
}