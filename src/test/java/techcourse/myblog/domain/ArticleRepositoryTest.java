package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleRepositoryTest {
    static ArticleRepository articleRepository = new ArticleRepository();

    @BeforeEach
    void deleteAll() {
        articleRepository.deleteAll();
    }

    @Test
    void 한_개_저장() {
        Article article = new Article("title", "url", "contents");
        articleRepository.save(article);
        assertThat(articleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 두_개_저장() {
        articleRepository.save(new Article("title", "url", "contents"));
        articleRepository.save(new Article("title", "url", "contents"));

        assertThat(articleRepository.findAll().size()).isEqualTo(2);
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

}