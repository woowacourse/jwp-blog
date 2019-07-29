package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserDto;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void 작성() {
        UserDto userDto = UserDto.builder().name("test")
                .email("test@test.com").password("testtset123").build();

        User user = testEntityManager.persist(userDto.toEntity());
        testEntityManager.flush();
        testEntityManager.clear();

        ArticleDto articleDto = ArticleDto.builder().userDto(UserDto.from(user)).build();
        Article article = articleDto.toEntity();

        Article persistArticle = testEntityManager.persist(article);
        testEntityManager.flush();
        testEntityManager.clear();

        Article repoArticle = articleRepository.findById(persistArticle.getId()).get();

        assertThat(repoArticle).isEqualTo(article);
    }
}