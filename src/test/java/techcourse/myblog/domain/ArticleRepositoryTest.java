package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.IllegalIdException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    static ArticleRepository articleRepository = new ArticleRepository();

    @BeforeEach
    void deleteAll() {
        articleRepository.deleteAll();
    }

    @Test
    void 저장() {
        Article article = new Article("title", "url", "contents");
        articleRepository.save(article);
        assertThat(articleRepository.findById(0).get()).isEqualTo(article);
    }

    @Test
    void 수정() {
        Article originalArticle = new Article("hello", "url", "contents");

        articleRepository.save(new Article("hello", "url", "contents"));
        articleRepository.save(originalArticle);

        Article newArticle = new Article("bye", "url", "contents");
        newArticle.setId(1);

        articleRepository.update(newArticle);

        assertThat(articleRepository.findById(1).get().getTitle()).isEqualTo("bye");
    }

    @Test
    void 존재하는_id_조회() {
        assertThrows(IllegalIdException.class, () -> {
            articleRepository.findById(0);
        });
    }

}