package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest {
    private static final int TEST_ARTICLE_ID = 1;

    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    public void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article();
        article.setId();
        article.setTitle("title");
        article.setCoverUrl("coverUrl");
        article.setContents("contents");
        articleRepository.addArticle(article);
    }

    @Test
    public void 게시글을_저장한다() {
        Article newArticle = new Article();
        article.setId();
        article.setTitle("title");
        article.setCoverUrl("coverUrl");
        article.setContents("contents");
        articleRepository.addArticle(newArticle);

        assertThat(articleRepository.findAll()).contains(newArticle);
    }

    @Test
    public void 게시글을_조회한다() {
        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID)).isEqualTo(article);
    }

    @Test
    public void 게시글을_수정한다() {
        Article editedArticle = new Article();
        article.setTitle("newTitle");
        article.setCoverUrl("newCoverUrl");
        article.setContents("newContents");
        articleRepository.editArticle(TEST_ARTICLE_ID, editedArticle);

        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getTitle()).isEqualTo(editedArticle.getTitle());
        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(articleRepository.findArticleById(TEST_ARTICLE_ID).getContents()).isEqualTo(editedArticle.getContents());
    }

    @Test
    public void 게시글을_삭제한다() {
        articleRepository.deleteArticle(TEST_ARTICLE_ID);

        assertThatThrownBy(() -> {
            articleRepository.findArticleById(TEST_ARTICLE_ID);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 존재하지_않는_게시글을_수정하려_시도하면_예외를_던진다() {
        assertThatThrownBy(() -> {
            articleRepository.editArticle(2, article);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}