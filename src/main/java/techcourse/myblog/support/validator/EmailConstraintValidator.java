package techcourse.myblog.support.validator;

import techcourse.myblog.application.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<EmailConstraint, String> {
    private final UserService userService;

    public EmailConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (userService.isEmailExist(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("중복된 이메일입니다!")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
