package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ArticleRepositoryTest {
    ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    public void find_all_article() {
        Article article = new Article(0,"title", "url", "contents");
        articleRepository.create(article);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article));
    }

    @Test
    public void add_article() {
        Article article = new Article(0,"title", "url", "contents");
        articleRepository.create(article);
        articleRepository.create(article);
        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void update_article() {
        Article article = new Article(0,"title", "url", "contents");
        articleRepository.create(article);
        articleRepository.update(article.getId(),new Article(0,"title2", "url2", "contents2"));
        assertThat(articleRepository.findById(article.getId())).isEqualTo(new Article(0,"title2", "url2", "contents2"));
    }

    @Test
    public void find_article_by_id() {
        Article article = new Article(0,"title", "url", "contents");
        articleRepository.create(article);
        assertThat(articleRepository.findById(0)).isEqualTo(article);
    }

}