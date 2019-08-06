package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest {
    private ArticleRepository articleRepository = new ArticleRepository();

    @Test
    public void 게시글을_저장한다() {
        Article newArticle = new Article("newTitle", "newCoverUrl", "newContents");
        articleRepository.addArticle(newArticle);

        assertThat(articleRepository.findAll()).contains(newArticle);
    }

    @Test
    public void 게시글을_조회한다() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);

        assertThat(articleRepository.findArticleById(article.getId())).isEqualTo(article);
    }

    @Test
    public void 게시글을_수정한다() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);
        articleRepository.editArticle(article.getId(), "newTitle", "newCoverUrl", "newContents");

        assertThat(articleRepository.findArticleById(article.getId()).getTitle()).isEqualTo("newTitle");
        assertThat(articleRepository.findArticleById(article.getId()).getCoverUrl()).isEqualTo("newCoverUrl");
        assertThat(articleRepository.findArticleById(article.getId()).getContents()).isEqualTo("newContents");
    }

    @Test
    public void 게시글을_삭제한다() {
        Article article = new Article("title", "coverUrl", "contents");
        articleRepository.addArticle(article);
        articleRepository.deleteArticle(article.getId());

        assertThatThrownBy(() -> {
            articleRepository.findArticleById(article.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 존재하지_않는_게시글을_수정하려_시도하면_예외를_던진다() {
        assertThatThrownBy(() -> {
            articleRepository.editArticle(100, "newTitle", "newCoverUrl", "newContents");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}