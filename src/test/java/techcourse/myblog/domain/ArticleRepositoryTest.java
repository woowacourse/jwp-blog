package techcourse.myblog.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleRepositoryTest {
    @Test
    @DisplayName("게시물 등록하는 테스트")
    void save() {
        ArticleRepository articleRepository = new ArticleRepository();
        Article article = new Article();
        article.setTitle("test title");
        article.setContent("test content");
        article.setCoverUrl("test url");
        articleRepository.save(article);
        assertThat(articleRepository.findAll()).contains(article);
    }
}