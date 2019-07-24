package techcourse.myblog.support;

import org.junit.jupiter.api.BeforeEach;
import techcourse.myblog.domain.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void checkValidate(Object object, boolean check, Class<?> ...groups) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
        assertEquals(violations.isEmpty(), check);
    }
}
