package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleRepository.insert(new Article(0, "title", "url", "contents"));
    }

    @Test
    void 게시글_정상_추가() {
        int id = articleRepository.nextId();
        Article article = new Article(id, "title2", "url2", "contents2");
        articleRepository.insert(article);
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    void 게시글_정상_검색() {
        assertDoesNotThrow(() -> articleRepository.findById(0));
    }

    @Test
    void 존재하지_않는_게시글_검색_에러() {
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.findById(1));
    }

    @Test
    void 존재하는_게시글_정상_수정() {
        Article targetArticle = new Article(0, "tilte2", "url2", "contents2");
        articleRepository.update(targetArticle);
        assertEquals(articleRepository.findById(0), targetArticle);
    }

    @Test
    void 존재하지않는_게시글_수정_시도_에러() {
        assertThrows(ArticleNotFoundException.class, () ->
                articleRepository.update(new Article(articleRepository.nextId(), "title2", "url2", "contents2")));
    }

    @Test
    void 존재하는_게시글_정상_삭제() {
        assertDoesNotThrow(() -> articleRepository.remove(0));
    }

    @Test
    void 존재하지않는_게시글_삭제_시도_에러() {
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.remove(articleRepository.nextId()));
    }
}