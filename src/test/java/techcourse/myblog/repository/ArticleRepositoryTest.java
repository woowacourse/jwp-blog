package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.exception.CouldNotFindArticleIdException;

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
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("test title");
        articleDto.setCoverUrl("test coverUrl");
        articleDto.setContents("test contents");

        article = articleRepository.save(Article.of(articleDto));
    }

    @Test
    @DisplayName("게시물을 생성하고 저장한다.")
    void createTest() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("saveTest title");
        articleDto.setCoverUrl("saveTest coverUrl");
        articleDto.setContents("saveTest contents");

        Article test = articleRepository.save(Article.of(articleDto));

        assertThat(test).isNotNull();
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void readTest() {
        Optional<Article> findArticle = articleRepository.findById(article.getArticleId());
        assertThat(findArticle.isPresent()).isTrue();
        assertThat(findArticle.get()).isEqualTo(article);
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void readFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository
                .findById(TEST_ARTICLE_ID_NOT_IN_REPO)
                .orElseThrow(CouldNotFindArticleIdException::new)
        );
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 업데이트 한다.")
    void updateByIdTest() {
        Article findArticle = articleRepository.findById(article.getArticleId()).get();
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("update title");
        articleDto.setCoverUrl("update coverUrl");
        articleDto.setContents("update contents");

        findArticle.updateArticle(articleDto);
        Article updateArticle = articleRepository.save(findArticle);

        assertThat(updateArticle.getTitle()).isEqualTo("update title");
        assertThat(updateArticle.getCoverUrl()).isEqualTo("update coverUrl");
        assertThat(updateArticle.getContents()).isEqualTo("update contents");
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

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }
}