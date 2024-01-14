package ru.clevertec.houses.validator.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.stereotype.Component;
import ru.clevertec.houses.dto.PersonDto;

import java.util.Set;

@Component
public class PersonDtoValidator implements DtoValidator<PersonDto> {

    @Override
    public boolean isValid(@NotNull PersonDto personDto) throws ValidationException {
        if (personDto == null) {
            throw new ValidationException();
        }

        Validator validator;
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            validator = factory.getValidator();
        }

        Set<ConstraintViolation<PersonDto>> violations = validator.validate(personDto);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }

        return true;
    }

    @Override
    public boolean isValidWithNotNullUuid(PersonDto personDto) throws ValidationException {
        if (personDto.uuid == null) {
            throw new ValidationException();
        }

        return isValid(personDto);
    }

}
