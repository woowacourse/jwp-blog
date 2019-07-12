package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void 게시글_정상_추가() {
        Article article = new Article(0, "title", "url", "contents");
        articleRepository.insert(article);
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    void 게시글_정상_검색() {
        Article article = new Article(0, "title", "url", "contents");
        articleRepository.insert(article);
        assertThat(articleRepository.find(0)).isEqualTo(article);
    }

    @Test
    void 존재하지_않는_게시글_검색_에러() {
        assertThrows(IllegalArgumentException.class, () ->
                articleRepository.find(0));
    }

    @Test
    void 존재하는_게시글_정상_수정() {
        int id = articleRepository.nextId();
        articleRepository.insert(new Article(id, "title", "url", "contents"));
        assertDoesNotThrow(() ->
                articleRepository.update(new Article(id, "a", "a", "a")));
    }

    @Test
    void 존재하지않는_게시글_수정_시도_에러() {
        assertThrows(IllegalArgumentException.class, () ->
                articleRepository.update(new Article(articleRepository.nextId(), "a", "a", "a")));
    }

    @Test
    void 존재하는_게시글_정상_삭제() {
        int id = articleRepository.nextId();
        articleRepository.insert(new Article(id, "title", "url", "contents"));
        assertDoesNotThrow(() -> articleRepository.remove(id));
    }

    @Test
    void 존재하지않는_게시글_삭제_시도_에러() {
        assertThrows(IllegalArgumentException.class, () -> articleRepository.remove(0));
    }
}