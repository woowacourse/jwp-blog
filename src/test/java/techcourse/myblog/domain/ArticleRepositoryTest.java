package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ArticleRepositoryTest {
    private static final String UPDATED_TITLE = "sloth is the best!";
    private static final Long NOT_EXIST_ARTICLE_ID = 0L;
    private static final Long UPDATED_ARTICLE_ID = 1L;

    private ArticleRepository articleRepository;
    private Article article;

    private final String title = "슬로헴";
    private final String contents = "live like sloth, ehem!";
    private final String coverUrl = "/image/sloth.jpg";

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article(title, contents, coverUrl);

        articleRepository.addArticle(article);
    }

    @Test
    void 게시글_등록_성공_테스트() {
        assertThat(articleRepository.findAll().contains(article)).isTrue();
    }

    @Test
    void 게시글_조회_성공_테스트() {
        articleRepository.findArticleById(article.getArticleId())
                .ifPresent(a -> assertThat(a).isEqualTo(article));
    }

    @Test
    void 존재하지_않는_게시글_조회_실패_테스트() {
        articleRepository.findArticleById(NOT_EXIST_ARTICLE_ID)
                .ifPresent(a -> assertThat(a).isEqualTo(Optional.empty()));
    }

    @Test
    void 전체글_조회_성공_테스트() {
        Article article2 = new Article(title, contents, coverUrl);

        articleRepository.addArticle(article2);

        List<Article> expected = Arrays.asList(article, article2);
        List<Article> actual = articleRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void articleId와_일치하는_게시글_삭제_확인_테스트() {
        articleRepository.deleteArticle(article.getArticleId());

        assertThat(articleRepository.findAll().contains(article)).isFalse();
    }

    @Test
    void 게시글_수정_성공_테스트() {
        Article updatedArticle = new Article(UPDATED_TITLE, contents, coverUrl);

        articleRepository.updateArticle(UPDATED_ARTICLE_ID, updatedArticle);

        articleRepository.findArticleById(UPDATED_ARTICLE_ID)
                .ifPresent(a -> assertThat(a.equals(updatedArticle)).isTrue());
    }

    @AfterEach
    void tearDown() {
        articleRepository.findAll().clear();
    }
}
