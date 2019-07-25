package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    private static final long UNASSIGNED_ARTICLE_ID = 0L;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void 게시글_저장이_잘_되는지_확인() {
        Article article = new Article(UNASSIGNED_ARTICLE_ID, "title", "coverUrl", "contents");
        articleRepository.save(article);

        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    public void 게시글_수정이_잘_되는지_확인() {
        Article article = new Article(UNASSIGNED_ARTICLE_ID, "title", "coverUrl", "contents");
        articleRepository.save(article);
        Article newArticle = new Article(article.getArticleId(), "new_title", "new_coverUrl", "new_contents");
        articleRepository.save(newArticle);

        assertThat(articleRepository.findAll()).contains(newArticle);
        assertThat(articleRepository.findAll()).doesNotContain(article);
    }

    @Test
    public void 게시글_삭제가_잘_되는지_확인() {
        Article article = new Article(UNASSIGNED_ARTICLE_ID, "title", "coverUrl", "contents");
        articleRepository.deleteById(articleRepository.save(article).getArticleId());

        assertThat(articleRepository.findAll()).doesNotContain(article);
    }

    @Test
    public void 존재하지_않는_게시글_삭제하는_경우_예외처리() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> articleRepository.deleteById(110L));
    }
}