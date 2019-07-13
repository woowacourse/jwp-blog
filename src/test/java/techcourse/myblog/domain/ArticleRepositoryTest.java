package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {
    private static Article article = new Article("test title", "test contents", "test cover url");
    private static ArticleRepository articleRepository = new ArticleRepository(new ArrayList<>(Collections.singletonList(article)));

    @Test
    void saveTest() {
        assertThat(articleRepository.findAll())
                .isEqualTo(new ArrayList<>(Collections.singletonList(article)));
    }

    @Test
    void findByIdTest() {
        assertThat(articleRepository.findById(1)).isEqualTo(article);
    }

    @Test
    void getSizeTest() {
        assertThat(articleRepository.getSize()).isEqualTo(1);
    }

    @Test
    void updateTest() {
        Article updatedArticle = new Article("updated title", "updated contents", "updated cover url");
        articleRepository.update(1, updatedArticle);
        assertThat(articleRepository.findById(1).getTitle()).isEqualTo(updatedArticle.getTitle());
        assertThat(articleRepository.findById(1).getContents()).isEqualTo(updatedArticle.getContents());
        assertThat(articleRepository.findById(1).getCoverUrl()).isEqualTo(updatedArticle.getCoverUrl());
    }

    @Test
    void getLatestArticleTest() {
        assertThat(articleRepository.getLatestArticle()).isEqualTo(article);
    }

    @Test
    void deleteTest() {
        articleRepository.delete(1);
        assertThat(articleRepository.findAll().contains(article)).isFalse();
        articleRepository.save(article);
    }
}