package com.backend.seabook.validation;

import com.github.slugify.Slugify;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class ValidSlugValidator implements ConstraintValidator<ValidSlug, String> {
    private final static String SLUG_PATTERN = "^[a-z0-9-]+$";
    private final static Pattern PATTERN = Pattern.compile(SLUG_PATTERN);

    @Override
    public void initialize(ValidSlug constraintAnnotation) {
    }

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext constraintValidatorContext) {
        return slug.isEmpty() || (PATTERN.matcher(slug).matches() && isClear(slug));
    }

    private boolean isClear(String slug) {
        final Slugify slg = Slugify.builder().build();
        final String result = slg.slugify(slug);
        return result.equals(slug);
    }
}
