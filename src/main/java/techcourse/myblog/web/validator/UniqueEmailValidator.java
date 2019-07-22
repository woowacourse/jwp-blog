package techcourse.myblog.web.validator;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> found = userRepository.findByEmail(email);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("이미 등록된 이메일입니다")
            .addConstraintViolation();

        return !found.isPresent();
    }
}
