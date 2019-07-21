package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.error.NotFoundArticleIdException;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class ArticleRepositoryTest {
    ArticleRepository articleRepository;
    Article article;
    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article(0,"title", "url", "contents");
    }

    @Test
    public void find_all_article() {
        articleRepository.create(article);
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(article));
    }

    @Test
    public void add_article() {
        articleRepository.create(article);
        assertThat(articleRepository.findAll().contains(article)).isTrue();
    }

    @Test
    public void add_multiple_article() {
        articleRepository.create(article);
        articleRepository.create(article);
        assertThat(articleRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void update_article() {
        articleRepository.create(article);
        articleRepository.update(article.getId(),new Article(0,"title2", "url2", "contents2"));
        assertThat(articleRepository.findById(article.getId())).isEqualTo(new Article(0,"title2", "url2", "contents2"));
    }

    @Test
    public void find_article_by_id() {
        articleRepository.create(article);
        assertThat(articleRepository.findById(0)).isEqualTo(article);
    }

    @Test
    public void find_article_by_id_not_found(){
        articleRepository.create(article);
        assertThatThrownBy(()->{
            articleRepository.findById(1);
        }).isInstanceOf(NotFoundArticleIdException.class);
    }

    @Test
    public void delete_article(){
        articleRepository.create(article);
        articleRepository.delete(0);
        assertThat(articleRepository.findAll().contains(article)).isFalse();

    }
}