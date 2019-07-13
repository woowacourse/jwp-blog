package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {
    private static final int FIRST_INDEX = 1;

    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article("title", "contents", "coverUrl");
        articleRepository.save(article);
    }

    @Test
    void save() {
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        assertThat(articleRepository.findAll()).isEqualTo(articles);
    }

    @Test
    void findById() {
        assertThat(articleRepository.findById(FIRST_INDEX)).isEqualTo(article);
    }

    @Test
    void update() {
        Article updatedArticle = new Article("title1", "content1", "coverUrl1");

        articleRepository.update(1, updatedArticle);

        assertThat(articleRepository.findById(FIRST_INDEX).getTitle()).isEqualTo(updatedArticle.getTitle());
        assertThat(articleRepository.findById(FIRST_INDEX).getContents()).isEqualTo(updatedArticle.getContents());
        assertThat(articleRepository.findById(FIRST_INDEX).getCoverUrl()).isEqualTo(updatedArticle.getCoverUrl());
    }

    @Test
    void getLatestArticle() {
        assertThat(articleRepository.getLatestArticle()).isEqualTo(article);
    }

    @Test
    void delete() {
        articleRepository.delete(FIRST_INDEX);

        assertThat(articleRepository.getSize()).isEqualTo(0);
    }

    @AfterEach
    void tearDown() {
        Article.initCurrentId();
    }
}