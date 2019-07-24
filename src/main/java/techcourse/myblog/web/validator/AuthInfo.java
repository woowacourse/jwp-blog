package techcourse.myblog.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthInfoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthInfo {

    String message() default "Login failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
