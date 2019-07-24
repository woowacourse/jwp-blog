package techcourse.myblog.articles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void save() {
        Article article = Article.builder()
                .title("title")
                .contents("contents")
                .coverUrl("coverUrl")
                .build();

        assertThat(articleRepository.save(article)).isNotNull();
    }

    @Test
    void update() {
        Article article = Article.builder()
                .title("title")
                .contents("contents")
                .coverUrl("coverUrl")
                .build();

        Article savedArticle = articleRepository.save(article);

        Article editedArticle = Article.builder()
                .id(savedArticle.getId())
                .title("1")
                .contents("2")
                .coverUrl("3")
                .build();

        BeanUtils.copyProperties(editedArticle, savedArticle);
        savedArticle = articleRepository.save(savedArticle);

        assertThat(savedArticle.getContents()).isEqualTo(editedArticle.getContents());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(savedArticle.getTitle()).isEqualTo(editedArticle.getTitle());
    }
}
