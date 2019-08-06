package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    public void comment_정상_생성() {
        assertDoesNotThrow(() -> new Comment("내용"));
    }

    @Test
    public void comment_비어있는_경우_Validation_확인() {
        Comment comment = new Comment("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        assertThat(violations.isEmpty()).isFalse();
        violations.forEach(violation ->
                assertThat(violation.getMessage()).isEqualTo("내용을 입력해주세요.")
        );
    }
}