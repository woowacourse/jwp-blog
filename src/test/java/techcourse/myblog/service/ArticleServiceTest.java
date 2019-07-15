package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleServiceTest {
    private ArticleService service;
    private Article article;

    @BeforeEach
    void setUp() {
        ArticleRepository repository = new ArticleRepository();
        service = new ArticleService(repository);
        article = new Article(1, "title", "", "content");
    }

    @Test
    void 생성자_오류_확인_repository가_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ArticleService(null));
    }

    @Test
    void 게시글_생성_확인() {
        assertThat(service.createArticle(article.convertToDTO())).isEqualTo(1);
    }

    @Test
    void 게시글_생성_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.createArticle(null));
    }

    @Test
    void 게시글_조회_확인() {
        service.createArticle(article.convertToDTO());
        assertThat(service.findArticleById(1)).isEqualTo(article.convertToDTO());
    }

    @Test
    void 모든_게시글_조회_확인() {
        service.createArticle(article.convertToDTO());
        assertThat(service.findAll()).isEqualTo(Arrays.asList(article.convertToDTO()));
    }

    @Test
    void 게시글_수정_확인() {
        service.createArticle(article.convertToDTO());
        Article newArticle = new Article(1, "newTitle", "", "newContent");
        service.updateArticle(1, newArticle.convertToDTO());
        assertThat(service.findArticleById(1)).isEqualTo(newArticle.convertToDTO());
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null일_경우() {
        service.createArticle(article.convertToDTO());
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.updateArticle(1, null));
    }

    @Test
    void 게시글_삭제_확인() {
        service.createArticle(article.convertToDTO());
        service.deleteArticle(1);
        assertThat(service.findAll().contains(article.convertToDTO())).isFalse();
    }
}
