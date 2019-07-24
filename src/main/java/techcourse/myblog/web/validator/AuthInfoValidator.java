package techcourse.myblog.web.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import techcourse.myblog.service.UserAuthenticateException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.dto.LoginRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AuthInfoValidator implements ConstraintValidator<AuthInfo, LoginRequestDto> {

    private static Logger logger = LoggerFactory.getLogger(AuthInfoValidator.class);

    private final UserService userService;

    public AuthInfoValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(AuthInfo constraintAnnotation) {

    }

    @Override
    public boolean isValid(LoginRequestDto authRequest, ConstraintValidatorContext context) {
        boolean isLoginSucceed = false;
        try {
            userService.authenticate(authRequest);
            isLoginSucceed = true;
        } catch (UserAuthenticateException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이메일이나 비밀번호가 올바르지 않습니다")
                .addConstraintViolation();
            logger.info("Login failed with: '{}' '{}'",
                authRequest.getEmail(), authRequest.getPassword());
        } catch (Exception e) {
            logger.error("Error occurred while authenticate", e);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("서버 에러입니다")
                .addConstraintViolation();
        }

        return isLoginSucceed;
    }
}
