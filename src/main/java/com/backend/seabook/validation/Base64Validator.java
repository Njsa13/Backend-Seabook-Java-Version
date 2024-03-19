package com.backend.seabook.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class Base64Validator implements ConstraintValidator<Base64Image, String> {
    @Override
    public void initialize(Base64Image constraintAnnotation) {
    }

    @Override
    public boolean isValid(String img, ConstraintValidatorContext constraintValidatorContext) {
        if (img.isEmpty()) {
            return true;
        }
        try {
            byte[] decodedBytes = Base64.getMimeDecoder().decode(img.split(",")[1]);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            log.info("Image is {}", decodedBytes);
            return image != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
