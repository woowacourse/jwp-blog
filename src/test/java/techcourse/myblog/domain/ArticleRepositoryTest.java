package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleRequestDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article newArticle;
    private ArticleRequestDto articleRequestDto;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("1");
        articleRequestDto.setCoverUrl("2");
        articleRequestDto.setContents("3");
        articleRequestDto.setCategory("4");
        newArticle = Article.of(articleRequestDto);

    }

    @Test
    void addArticle() {
        articleRepository.addArticle(newArticle);

        assertThat(articleRepository.findAll()).hasSize(1);
    }

    @Test
    void findById() {
        articleRepository.addArticle(newArticle);
        Optional<Article> maybeArticle = articleRepository.findById(newArticle.getId());

        assertThat(maybeArticle.isPresent()).isTrue();
        assertThat(maybeArticle.get()).isEqualTo(newArticle);
    }

    @Test
    void 수정_테스트() {
        articleRepository.addArticle(newArticle);
        Article articleFound = articleRepository.findById(newArticle.getId())
                .orElseThrow(IllegalStateException::new);
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("changed title");

        articleFound.update(articleRequestDto);
        articleFound = articleRepository.findById(newArticle.getId())
                .orElseThrow(IllegalStateException::new);

        assertThat(articleFound.getTitle()).isEqualTo("changed title");
    }

    @Test
    void delete() {
        articleRepository.addArticle(newArticle);
        articleRepository.deleteById(newArticle.getId());

        assertThat(articleRepository.findAll()).hasSize(0);
    }

    @Test
    void Article중에_해당_카테고리가_있는지_테스트() {
        articleRepository.addArticle(newArticle);
        assertThat(articleRepository.hasCategory("4")).isTrue();
    }
}
