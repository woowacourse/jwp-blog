package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article("title", "coverUrl", "contents");
    }

    @Test
    public void 게시글_저장이_잘_되는지_확인() {
        articleRepository.save(article);

        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    public void 게시글_수정이_잘_되는지_확인() {
        Article newArticle = new Article(article.getArticleId(), "new_title", "new_coverUrl", "new_contents");

        articleRepository.save(newArticle);

        assertThat(articleRepository.findAll()).contains(newArticle);
    }

    @Test
    public void 게시글_삭제가_잘_되는지_확인() {
        articleRepository.save(article);
        articleRepository.deleteById(article.getArticleId());

        assertThat(articleRepository.findAll()).doesNotContain(article);
    }

    @Test
    public void 존재하지_않는_게시글_삭제하는_경우_예외처리() {
        Article inexistedArticle = new Article("new_title", "new_coverUrl", "new_contents");

        assertThrows(EmptyResultDataAccessException.class,
                () -> articleRepository.deleteById(inexistedArticle.getArticleId()));
    }
}