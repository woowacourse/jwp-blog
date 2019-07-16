package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.excerption.ArticleNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleRepositoryTest {
    private ArticleRepository repository;
    private Article article;
    private Article persistArticle;

    @BeforeEach
    void setUp() {
        repository = new ArticleRepository();
        article = new Article(1, "title", "", "content");
        persistArticle = repository.save(article);
    }

    @Test
    void 게시글_추가_확인() {
        Article persistArticle = repository.save(article);
        assertThat(persistArticle).isEqualTo(article);
    }

    @Test
    void 게시글_추가_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> repository.save(null));
    }

    @Test
    void 게시글_조회_확인() {
        Article retrieveArticle = repository.findById(1);
        assertThat(retrieveArticle).isEqualTo(persistArticle);
    }

    @Test
    void 게시글_조회_오류확인_해당하는_id가_없을_경우() {
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> repository.findById(2));
    }

    @Test
    void 모든_게시글_조회_확인() {
        List<Article> articles = repository.findAll();
        assertThat(articles).isEqualTo(Arrays.asList(persistArticle));
    }

    @Test
    void 게시글_수정_확인() {
        Article updateArticle = new Article(1, "newTitle", "", "newContent");
        repository.update(1, updateArticle);
        Article retrieveArticle = repository.findById(1);
        assertThat(retrieveArticle).isEqualTo(updateArticle);
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> repository.update(1, null));
    }

    @Test
    void 게시글_삭제_확인() {
        repository.delete(1);
        List<Article> articles = repository.findAll();
        assertThat(articles.contains(article)).isFalse();
    }
}
