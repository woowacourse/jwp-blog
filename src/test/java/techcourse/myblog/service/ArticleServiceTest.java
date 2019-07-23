package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Deprecated
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    private static Article article;

    static {
        article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();
    }

    @Test
    void create() {
        ArticleDto articleDto = new ArticleDto("title", "coverUrl", "contents");
        Long articleId = articleService.create(articleDto);
        assertTrue(articleRepository.findById(articleId).isPresent());
    }

    @Test
    void find() {
        Article savedArticle = articleRepository.save(article);
        Article foundArticle = articleService.findById(savedArticle.getId());
        assertThat(savedArticle.getTitle()).isEqualTo(foundArticle.getTitle());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(foundArticle.getCoverUrl());
        assertThat(savedArticle.getContents()).isEqualTo(foundArticle.getContents());
    }

    @Test
    void update() {
        Article savedArticle = articleRepository.save(article);
        ArticleDto articleDto = new ArticleDto("UpdateTitle", "UpdateCoverUrl", "UpdateContents");
        Article updatedArticle = articleService.update(savedArticle.getId(), articleDto);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleDto.getContents());
    }

    @Test
    void delete() {
        articleRepository.save(article);
        articleService.delete(article.getId());
        assertThat(articleRepository.findById(article.getId())).isEmpty();
    }
}