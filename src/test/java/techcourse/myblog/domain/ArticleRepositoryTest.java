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
        Article article = new Article();
        articleRepository.save(article);
        assertThat(articleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 두_개_저장() {
        articleRepository.save(new Article());
        articleRepository.save(new Article());

        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void 수정() {
        Article originalArticle = new Article();
        originalArticle.setTitle("hello");

        articleRepository.save(new Article());
        articleRepository.save(originalArticle);

        Article newArticle = new Article();
        newArticle.setId(1);
        newArticle.setTitle("bye");

        articleRepository.update(newArticle);

        assertThat(articleRepository.findById(1).get().getTitle()).isEqualTo("bye");

    }

}