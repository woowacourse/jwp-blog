package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article("title", "contents", "coverUrl");
    }

    @Test
    void save() {
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        articleRepository.save(article);

        assertThat(articleRepository.findAll()).isEqualTo(articles);
    }

    @Test
    void findById() {
        articleRepository.save(article);

        assertThat(articleRepository.findById(1)).isEqualTo(article);
    }

    @Test
    void update() {
        Article updatedArticle = new Article("title1", "content1", "coverUrl1");

        articleRepository.save(article);
        articleRepository.update(1, updatedArticle);

        assertThat(articleRepository.findById(1).getTitle()).isEqualTo(updatedArticle.getTitle());
        assertThat(articleRepository.findById(1).getContents()).isEqualTo(updatedArticle.getContents());
        assertThat(articleRepository.findById(1).getCoverUrl()).isEqualTo(updatedArticle.getCoverUrl());
    }

    @Test
    void getLatestArticle() {
        articleRepository.save(article);

        assertThat(articleRepository.getLatestArticle()).isEqualTo(article);
    }

    @Test
    void delete() {
        articleRepository.save(article);

        articleRepository.delete(1);

        assertThat(articleRepository.getSize()).isEqualTo(0);
    }

    @AfterEach
    void tearDown() {
        Article.initCurrentId();
    }
}