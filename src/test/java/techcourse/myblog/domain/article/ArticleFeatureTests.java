package techcourse.myblog.domain.article;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.domain.article.exception.IllegalContentsException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleFeatureTests {
    private final static String TITLE_CONSTRAINT_MESSAGE = "제목은 비어있을 수 없습니다.";
    private final static String CONTENTS_CONSTRAINT_MESSAGE = "내용은 비어있을 수 없습니다.";

    @ParameterizedTest(name = "{index}")
    @MethodSource("invalidArticleParameters")
    void ArticleDto_생성_검증(String title, String coverUrl, String contents, String msg) {
        assertThrows(IllegalContentsException.class, () -> new ArticleFeature(title, coverUrl, contents), msg);
    }

    static Stream<Arguments> invalidArticleParameters() {
        return Stream.of(
                Arguments.of("", "coverUrl", "contents", TITLE_CONSTRAINT_MESSAGE),
                Arguments.of("title", "coverUrl", "", CONTENTS_CONSTRAINT_MESSAGE)
        );
    }

}