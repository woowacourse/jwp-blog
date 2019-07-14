package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private int articleId;
    private Article article;
    private Article editedArticle;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleId = articleRepository.nextId();
        article = new Article(articleId, "title", "url", "contents");
        editedArticle = new Article(articleId, "a", "a", "a");
    }

    @Test
    void 게시글_정상_추가() {
        articleRepository.insert(article);
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    void 게시글_정상_검색() {
        articleRepository.insert(article);
        assertThat(articleRepository.find(articleId)).isEqualTo(article);
    }

    @Test
    void 존재하지_않는_게시글_검색_에러() {
        assertThrows(InvalidArticleException.class, () ->
                articleRepository.find(articleId));
    }

    @Test
    void 존재하는_게시글_정상_수정() {
        articleRepository.insert(article);
        assertDoesNotThrow(() ->
                articleRepository.update(editedArticle));
    }

    @Test
    void 존재하지않는_게시글_수정_시도_에러() {
        assertThrows(InvalidArticleException.class, () ->
                articleRepository.update(editedArticle));
    }

    @Test
    void 존재하는_게시글_정상_삭제() {
        articleRepository.insert(article);
        assertDoesNotThrow(() -> articleRepository.remove(articleId));
    }

    @Test
    void 존재하지않는_게시글_삭제_시도_에러() {
        assertThrows(InvalidArticleException.class, () ->
                articleRepository.remove(articleId));
    }
}