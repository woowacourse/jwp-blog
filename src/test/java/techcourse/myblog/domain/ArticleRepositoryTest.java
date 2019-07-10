package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ArticleRepositoryTest {

    @Test
    void 새로운_articleId_생성_확인_테스트() {
        ArticleRepository articleRepository = new ArticleRepository();
        Long actual = articleRepository.generateNewId();
        Long expected = 1l;
        assertThat(actual).isEqualTo(expected);
    }
}
