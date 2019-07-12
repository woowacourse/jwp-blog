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
        Article article = new Article("title", "url", "contents");
        articleRepository.add(article);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article));
    }

    @Test
    public void add_article() {
        Article article = new Article("title", "url", "contents");
        articleRepository.add(article);
        articleRepository.add(article);
        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void modify_article() {
        Article article = new Article("title", "url", "contents");
        articleRepository.add(article);
        article.setTitle("title2");
        article.setCoverUrl("url2");
        article.setTitle("contents2");
        articleRepository.modify(article);
        assertThat(articleRepository.findById(article.getId())).isEqualTo(new Article("title2", "url2", "contents2"));
    }

    @Test
    public void find_article_by_id() {
        Article article = new Article("title", "url", "contents");
        articleRepository.add(article);
        assertThat(articleRepository.findById(0)).isEqualTo(article);
    }

}