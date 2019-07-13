package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.excerption.ArticleNotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleRepositoryTest {
    private ArticleRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ArticleRepository();
    }

    @Test
    void 게시글_추가_확인() {
        assertThat(repository.addArticle(new Article(1, "title", "", "content"))).isEqualTo(1);
    }

    @Test
    void 게시글_추가_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> repository.addArticle(null));
    }

    @Test
    void 게시글_조회_확인() {
        Article article = new Article(1, "title", "", "content");
        repository.addArticle(article);
        assertThat(repository.findArticleById(1)).isEqualTo(article);
    }

    @Test
    void 게시글_조회_오류확인_해당하는_id가_없을_경우() {
        Article article = new Article(1, "title", "", "content");
        repository.addArticle(article);
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> repository.findArticleById(2));
    }

    @Test
    void 모든_게시글_조회_확인() {
        Article article = new Article(1, "title", "", "content");
        repository.addArticle(article);
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(article));
    }

    @Test
    void 게시글_수정_확인() {
        Article article = new Article(1, "title", "", "content");
        Article updateArticle = new Article(1, "newTitle", "", "newContent");
        repository.addArticle(article);
        repository.updateArticle(1, updateArticle);
        assertThat(repository.findArticleById(1)).isEqualTo(updateArticle);
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null인_경우() {
        Article article = new Article(1, "title", "", "content");
        repository.addArticle(article);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> repository.updateArticle(1, null));
    }

    @Test
    void 게시글_삭제_확인() {
        Article article = new Article(1, "title", "", "content");
        repository.addArticle(article);
        repository.deleteArticle(1);
        assertThat(repository.findAll().contains(article)).isFalse();
    }
}
