package ru.clevertec.houses.validator.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.stereotype.Component;
import ru.clevertec.houses.dto.HouseDto;

import java.util.Set;

@Component
public class HouseDtoValidator implements DtoValidator<HouseDto> {

    @Override
    public boolean isValid(@NotNull HouseDto houseDto) throws ValidationException {
        if (houseDto == null) {
            throw new ValidationException();
        }

        Validator validator;
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            validator = factory.getValidator();
        }

        Set<ConstraintViolation<HouseDto>> violations = validator.validate(houseDto);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }
        return true;
    }

    @Override
    public boolean isValidWithNotNullUuid(HouseDto houseDto) throws ValidationException {
        if (houseDto == null || houseDto.uuid == null) {
            throw new ValidationException();
        }

        return isValid(houseDto);
    }

}
