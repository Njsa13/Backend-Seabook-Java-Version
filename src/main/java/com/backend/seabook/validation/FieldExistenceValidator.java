package com.backend.seabook.validation;

import com.backend.seabook.exception.ServiceBusinessException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class FieldExistenceValidator implements ConstraintValidator<FieldExistence, Object> {
    private final JdbcTemplate jdbcTemplate;
    private String tableName;
    private String fieldName;
    private boolean shouldExist;

    @Override
    public void initialize(FieldExistence constraintAnnotation) {
        this.tableName = constraintAnnotation.tableName();
        this.fieldName = constraintAnnotation.fieldName();
        this.shouldExist = constraintAnnotation.shouldExist();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + "=" + "?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, o);
            return shouldExist ? count > 0 : count == 0;
        } catch (Exception e) {
            log.error("Failed to check field exists");
            throw new ServiceBusinessException("Failed to check field exists");
        }
    }
}
