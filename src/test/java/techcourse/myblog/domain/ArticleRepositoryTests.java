package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.NotFoundArticleException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTests {
    private ArticleRepository articleRepository;
    private static Article articleFirst = Article.builder()
            .id(1).title("타이틀1").coverUrl("유알엘1").contents("컨텐츠1").build();
    private static Article articleSecond = Article.builder()
            .id(2).title("타이틀2").coverUrl("유알엘2").contents("컨텐츠2").build();
    private static Article articleThird = Article.builder()
            .id(3).title("타이틀3").coverUrl("유알엘3").contents("컨텐츠3").build();

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();

        articleRepository.save(articleFirst);
        articleRepository.save(articleSecond);
        articleRepository.save(articleThird);
    }

    @Test
    void 첫_번째_게시물_조회_테스트() {
        assertThat(articleRepository.findById(1)).isEqualTo(articleFirst);
    }

    @Test
    void 마지막_게시물_조회_테스트() {
        assertThat(articleRepository.findById(3)).isEqualTo(articleThird);
    }

    @Test
    void 존재하지_않는_게시물_조회시_예외처리() {
        assertThrows(NotFoundArticleException.class, () -> articleRepository.findById(4));
    }

    @Test
    void 전체_조회_테스트() {
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(articleFirst, articleSecond, articleThird));
    }

    @Test
    void 게시물_추가_테스트() {
        Article newArticle = Article.builder()
                .id(4).title("타이틀4").coverUrl("유알엘4").contents("컨텐츠4").build();
        articleRepository.save(newArticle);
        assertThat(articleRepository.findById(4)).isEqualTo(newArticle);
    }

    @Test
    void 게시물_수정_테스트() {
        Article updatedArticle = Article.builder()
                .id(1).title("수정타이틀1").coverUrl("수정유알엘1").contents("수정컨텐츠1").build();
        articleRepository.update(updatedArticle);

        assertThat(articleRepository.findById(1)).isEqualTo(updatedArticle);
    }

    @Test
    void 게시물_삭제_테스트() {
        articleRepository.delete(1);
        assertThrows(NotFoundArticleException.class, () -> articleRepository.findById(1));
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}
