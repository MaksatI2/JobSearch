package org.example.JobSearch.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.JobSearch.model.AccountType;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountType accountType) {
        if (accountType == null) {
            return null;
        }
        return accountType.getId();
    }

    @Override
    public AccountType convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return AccountType.getById(id);
    }
}
