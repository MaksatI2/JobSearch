package org.example.JobSearch.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.JobSearch.model.AccountType;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, Long> {

    @Override
    public Long convertToDatabaseColumn(AccountType accountType) {
        if (accountType == null) {
            return null;
        }
        return accountType.getId();
    }

    @Override
    public AccountType convertToEntityAttribute(Long id) {
        if (id == null) {
            return null;
        }
        return AccountType.getById(id);
    }
}
