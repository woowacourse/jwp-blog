package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.validator.CouldNotFindArticleIdException;
import techcourse.myblog.web.dto.ArticleDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    private static final long TEST_ARTICLE_ID_NOT_IN_REPO = 2;

    @Autowired
    private ArticleRepository articleRepository;

    private Article article;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = ArticleDto.of("test title",
                "test coverUrl",
                "test contents"
        );

        article = articleRepository.save(new Article(articleDto));
    }

    @Test
    @DisplayName("게시물을 저장한다.")
    void saveTest() {
        ArticleDto articleDto = ArticleDto.of(
                "saveTest title",
                "saveTest coverUrl",
                "saveTest contents"
        );
        Article test = articleRepository.save(new Article(articleDto));

        assertThat(test).isNotNull();
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        Optional<Article> findArticle = articleRepository.findById(article.getArticleId());
        assertThat(findArticle.isPresent()).isTrue();
        assertThat(findArticle.get()).isEqualTo(article);
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.deleteById(article.getArticleId());
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository
                .findById(article.getArticleId())
                .orElseThrow(CouldNotFindArticleIdException::new)
        );
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void findFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository
                .findById(TEST_ARTICLE_ID_NOT_IN_REPO)
                .orElseThrow(CouldNotFindArticleIdException::new)
        );
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }
}