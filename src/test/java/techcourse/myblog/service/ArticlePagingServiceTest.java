package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticlePagingRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticlePagingServiceTest {
    @Autowired
    private ArticlePagingService articlePagingService;

    @Autowired
    private ArticlePagingRepository articlePagingRepository;

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
        Long articleId = articlePagingService.create(articleDto);
        assertTrue(articlePagingRepository.findById(articleId).isPresent());
    }

    @Test
    void find() {
        Article savedArticle = articlePagingRepository.save(article);
        Article foundArticle = articlePagingService.findById(savedArticle.getId());
        assertThat(savedArticle.getTitle()).isEqualTo(foundArticle.getTitle());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(foundArticle.getCoverUrl());
        assertThat(savedArticle.getContents()).isEqualTo(foundArticle.getContents());
    }

    @Test
    void update() {
        Article savedArticle = articlePagingRepository.save(article);
        ArticleDto articleDto = new ArticleDto("UpdateTitle", "UpdateCoverUrl", "UpdateContents");
        Article updatedArticle = articlePagingService.update(savedArticle.getId(), articleDto);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleDto.getContents());
    }

    @Test
    void delete() {
        articlePagingRepository.save(article);
        articlePagingService.delete(article.getId());
        assertThat(articlePagingRepository.findById(article.getId())).isEmpty();
    }
}