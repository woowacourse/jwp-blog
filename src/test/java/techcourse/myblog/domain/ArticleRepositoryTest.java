package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {
    private ArticleRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ArticleRepository();
    }

    @Test
    void Article_생성_테스트() {
        Article article = new Article("title", "contents", "background");
        repository.addBlog(article);
        assertThat(repository.findAll().size()).isEqualTo(1);
    }

    @Test
    void Article_삭제_테스트() {
        Article article = new Article("title", "contents", "background");
        repository.addBlog(article);
        repository.delete(0);
        assertThat(repository.findAll().size()).isEqualTo(0);
    }

    @Test
    void Article_find_테스트() {
        Article expetedArticle = new Article("title", "contents", "background");
        repository.addBlog(expetedArticle);
        Article article = repository.find(0);
        assertThat(article.getTitle()).isEqualTo(expetedArticle.getTitle());
        assertThat(article.getContents()).isEqualTo(expetedArticle.getContents());
        assertThat(article.getBackground()).isEqualTo(expetedArticle.getBackground());
    }
}