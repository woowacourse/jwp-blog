package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article(articleRepository.nextId(), "title", "url", "contents");
        articleRepository.insert(article);
    }

    @Test
    void 게시글_정상_추가() {
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    void 게시글_정상_검색() {
        assertThat(articleRepository.findById(article.getId())).isEqualTo(article);
    }

    @Test
    void 존재하지_않는_게시글_검색_에러() {
        assertThrows(ArticleNotFoundException.class, () ->
                articleRepository.findById(articleRepository.nextId()));
    }

    @Test
    void 존재하는_게시글_정상_수정() {
        int id = article.getId();
        Article editedArticle = new Article(id, "a", "a", "a");
        assertDoesNotThrow(() -> articleRepository.update(editedArticle));
        assertThat(checkSameCondition(editedArticle, articleRepository.findById(id))).isTrue();
    }

    @Test
    void 존재하지않는_게시글_수정_시도_에러() {
        Article editedArticle = new Article(articleRepository.nextId(), "a", "a", "a");
        assertThrows(ArticleNotFoundException.class, () ->
                articleRepository.update(editedArticle));
    }

    @Test
    void 존재하는_게시글_정상_삭제() {
        assertDoesNotThrow(() -> articleRepository.remove(article.getId()));
    }

    @Test
    void 존재하지않는_게시글_삭제_시도_에러() {
        assertThrows(ArticleNotFoundException.class, () ->
                articleRepository.remove(articleRepository.nextId()));
    }

    private boolean checkSameCondition(Article newArticle, Article article) {
        return article.getId() == newArticle.getId()
                && article.getTitle().equals(newArticle.getTitle())
                && article.getCoverUrl().equals(newArticle.getCoverUrl())
                && article.getContents().equals(newArticle.getContents());
    }
}