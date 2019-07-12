package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ArticleRepositoryTest {
    private static final String UPDATED_TITLE = "sloth is the best!";
    private static final int NEXT_ARTICLE_ID = 1;
    private static final long NOT_EXIST_ARTICLE_ID = 1L;

    private ArticleRepository articleRepository;
    private Article article;

    private final Long articleId = 1L;
    private final String title = "슬로헴";
    private final String contents = "live like sloth, ehem!";
    private final String coverUrl = "/image/sloth.jpg";

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();

        article = new Article();
        article.setArticleId(articleId);
        article.setTitle(title);
        article.setContents(contents);
        article.setCoverUrl(coverUrl);
    }

    @Test
    void 게시글_등록_성공_테스트() {
        articleRepository.addArticle(article);

        assertThat(articleRepository.findAll().contains(article)).isTrue();
    }

    @Test
    void 게시글_조회_성공_테스트() {
        articleRepository.addArticle(article);

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
        Article article2 = new Article();
        article2.setArticleId(articleRepository.generateNewId());
        article2.setTitle(title);
        article2.setContents(contents);
        article2.setCoverUrl(coverUrl);

        articleRepository.addArticle(article);
        articleRepository.addArticle(article2);

        List<Article> expected = Arrays.asList(article, article2);
        List<Article> actual = articleRepository.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 새로운_articleId_생성_확인_테스트() {
        Long expected = articleRepository.generateNewId() + NEXT_ARTICLE_ID;
        articleRepository.addArticle(article);
        Long actual = articleRepository.generateNewId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void articleId와_일치하는_게시글_삭제_확인_테스트() {
        articleRepository.addArticle(article);
        articleRepository.deleteArticle(article.getArticleId());

        assertThat(articleRepository.findAll().contains(article)).isFalse();
    }

    @Test
    void 게시글_수정_성공_테스트() {
        String updatedTitle = UPDATED_TITLE;
        Article updatedArticle = new Article();
        updatedArticle.setArticleId(articleId);
        updatedArticle.setTitle(updatedTitle);
        updatedArticle.setContents(contents);
        updatedArticle.setCoverUrl(coverUrl);

        articleRepository.addArticle(article);
        articleRepository.updateArticle(updatedArticle);

        articleRepository.findArticleById(updatedArticle.getArticleId())
                .ifPresent(a -> assertThat(a).isEqualTo(updatedArticle));
    }

    @Test
    void 게시물_수정후_이전의_게시물_없는지_확인_테스트() {
        String updatedTitle = UPDATED_TITLE;
        Article updatedArticle = new Article();
        updatedArticle.setArticleId(articleId);
        updatedArticle.setTitle(updatedTitle);
        updatedArticle.setContents(contents);
        updatedArticle.setCoverUrl(coverUrl);

        articleRepository.addArticle(article);
        articleRepository.updateArticle(updatedArticle);

        articleRepository.findArticleById(article.getArticleId())
                .ifPresent(a -> assertThat(a.equals(article)).isFalse());
    }
}
