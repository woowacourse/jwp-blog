package techcourse.myblog.domain.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepositoryTest {
    private static final Long BASE_USER_ID = 1L;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("생성, 수정일자 등록 확인")
    public void checkLocalDate() {
        LocalDate now = LocalDate.now();

        User author = userRepository.findById(BASE_USER_ID).orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository
                .save(new Article("TITLE", "https://article.com", "CONTENTS", author));

        assertThat(article.toCreatedDate().isEqual(now)).isTrue();
        assertThat(article.toLastModifiedDate().isEqual(now)).isTrue();
    }
}