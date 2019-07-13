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

}