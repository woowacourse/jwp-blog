package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.ArticleToSaveNotFoundException;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;
import techcourse.myblog.exception.InvalidArticleIdException;

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
        assertThatExceptionOfType(ArticleToSaveNotFoundException.class)
                .isThrownBy(() -> repository.save(null))
                .withMessage("저장할 게시글이 없습니다.");
    }

    @Test
    void 게시글_조회_확인() {
        Article retrieveArticle = repository.findById(persistArticle.getId());
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
        Article updateArticle = new Article(persistArticle.getId(), "newTitle", "", "newContent");
        repository.update(persistArticle.getId(), updateArticle);
        Article retrieveArticle = repository.findById(1);
        assertThat(retrieveArticle).isEqualTo(updateArticle);
    }

    @Test
    void 게시글_수정_오류확인_id가_자연수가_아닌_경우() {
        Article updateArticle = new Article(persistArticle.getId(), "newTitle", "", "newContent");
        assertThatExceptionOfType(InvalidArticleIdException.class)
                .isThrownBy(() -> repository.update(0, updateArticle))
                .withMessage("적절한 ID가 아닙니다.");
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(ArticleToUpdateNotFoundException.class)
                .isThrownBy(() -> repository.update(persistArticle.getId(), null))
                .withMessage("업데이트 해야할 게시글이 없습니다.");
    }

    @Test
    void 게시글_삭제_확인() {
        repository.delete(1);
        List<Article> articles = repository.findAll();
        assertThat(articles.contains(article)).isFalse();
    }
}
