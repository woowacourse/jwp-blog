package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ArticleRepositoryTests {

    private ArticleRepository articleRepository;
    private Article article;
    private Article deletedArticle;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article("", "", "");
        deletedArticle = new Article("", "", "");
        articleRepository.saveArticle(article);
        articleRepository.saveArticle(deletedArticle);
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
        assertThat(articleRepository.getArticleById(1)).isEqualTo(article);
    }

    @Test
    void getArticleById_찾았는데_없는거() {
        assertThatThrownBy(() -> articleRepository.getArticleById(5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}