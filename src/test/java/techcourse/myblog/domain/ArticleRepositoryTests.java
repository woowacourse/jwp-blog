package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ArticleRepositoryTests {

    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article("", "", "");
        articleRepository.saveArticle(article);
    }

    @Test
    void findAll() {
        assertThat(articleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void saveArticle() {
        articleRepository.saveArticle(new Article("", "", ""));
        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void getArticleById() {
        assertThat(articleRepository.getArticleById(1)
                .orElseThrow(IllegalArgumentException::new)).isEqualTo(article);
    }

    @Test
    void getArticleById_찾았는데_없는거() {
        assertThatThrownBy(
                () -> articleRepository.getArticleById(5).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}