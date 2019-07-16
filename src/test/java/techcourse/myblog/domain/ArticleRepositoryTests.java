package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.web.dto.ArticleDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ArticleRepositoryTests {

    private ArticleRepository articleRepository;
    private ArticleDto articleDto;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleDto = new ArticleDto("", "", "");
        articleRepository.saveArticle(articleDto);
    }

    @Test
    void findAll() {
        assertThat(articleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void saveArticle() {
        articleRepository.saveArticle(new ArticleDto("", "", ""));
        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void getArticleById() {
        Article article = articleRepository.getArticleById(1).orElseThrow(IllegalArgumentException::new);
        assertThat(article.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(article.getContents()).isEqualTo(articleDto.getContents());
        assertThat(article.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
    }

    @Test
    void getArticleById_찾았는데_없는거() {
        assertThatThrownBy(
                () -> articleRepository.getArticleById(5).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}