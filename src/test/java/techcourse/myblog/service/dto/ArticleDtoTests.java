package techcourse.myblog.service.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.dto.ArticleDto.CONTENTS_CONSTRAINT_MESSAGE;
import static techcourse.myblog.service.dto.ArticleDto.TITLE_CONSTRAINT_MESSAGE;

class ArticleDtoTests {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest(name = "{index}")
    @MethodSource("invalidArticleParameters")
    void ArticleDto_생성_검증(String title, String coverUrl, String contents, String msg) {
        Set<ConstraintViolation<ArticleDto>> constraintViolations = validator.validate(new ArticleDto(title, coverUrl, contents));
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly(msg);
    }

    static Stream<Arguments> invalidArticleParameters() {
        return Stream.of(
                Arguments.of("", "coverUrl", "contents", TITLE_CONSTRAINT_MESSAGE),
                Arguments.of("title", "coverUrl", "", CONTENTS_CONSTRAINT_MESSAGE)
        );
    }

}