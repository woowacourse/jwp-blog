package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ArticleRepositoryTest {
    private Article article;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setUp() {
        article = new Article(0L, "title", "coverUrl", "contents");
    }

    @Test
    public void 게시글_저장이_잘_되는지_확인한다() {
        articleRepository.save(article);
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    public void 게시글_수정이_잘_되는지_확인한다() {
        articleRepository.save(article);
        Article updatedArticle = new Article(article.getArticleId(), "new_title", "new_coverUrl", "new_contents");
        article.update(updatedArticle);

        assertThat(articleRepository.findAll()).contains(updatedArticle);
    }

    @Test
    public void 게시글_삭제가_잘_되는지_확인한다() {
        articleRepository.deleteById(articleRepository.save(article).getArticleId());
        assertThat(articleRepository.findAll()).doesNotContain(article);
    }

    @Test
    public void 존재하지_않는_게시글을_삭제하는_경우_예외를_던진다() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> articleRepository.deleteById(110L));
    }
}