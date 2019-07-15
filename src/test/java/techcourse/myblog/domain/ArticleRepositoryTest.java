package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    void add() {
        articleRepository.add(new Article("title", "contents", "coverUrl"));
        assertThat(articleRepository.count()).isEqualTo(1);
    }

    @Test
    void get() {
        add();
        assertThat(articleRepository.get(0)).isEqualTo(new Article("title", "contents", "coverUrl"));
    }

    @Test
    void lastIndex() {
        add();
        assertThat(articleRepository.lastIndex()).isEqualTo(0);
    }

    @Test
    void count() {
        add();
        assertThat(articleRepository.count()).isEqualTo(1);
    }

    @Test
    void update() {
        add();
        articleRepository.update(0, new Article("update title", "update contents", "update coverUrl"));
        assertThat(articleRepository.get(0)).isEqualTo(new Article("update title", "update contents", "update coverUrl"));
    }

    @Test
    void remove() {
        add();
        articleRepository.remove(0);
        assertThat(articleRepository.count()).isEqualTo(0);
    }
}