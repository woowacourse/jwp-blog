package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private static final int ARTICLE_TEST_ID = 1;
    public static final int NOT_FIND_ARTICLE_ID = 2;
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article("test title", "test contents", "test Url", ARTICLE_TEST_ID);
    }

    @Test
    @DisplayName("게시물 등록하는 테스트")
    void save() {
        // When
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.findAll()).contains(article);
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        articleRepository.save(article);

        assertThat(articleRepository.find(ARTICLE_TEST_ID)).isEqualTo(article);
    }

    @Test
    @DisplayName("게시물 id가 없는 경우 예외를 던져준다.")
    void notFindTest() {
        articleRepository.save(article);

        assertThrows(IllegalArgumentException.class, () -> articleRepository.find(NOT_FIND_ARTICLE_ID));
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.save(article);

        articleRepository.delete(ARTICLE_TEST_ID);
        assertThrows(IllegalArgumentException.class, () -> articleRepository.find(ARTICLE_TEST_ID));
    }

    @Test
    void updateTest() {
        String updateTitle = "updated title";
        String updateCoverUrl = "updated coverUrl";
        String updateContents = "updated contents";
        articleRepository.save(article);
        articleRepository.update(
                ARTICLE_TEST_ID,
                updateTitle,
                updateCoverUrl,
                updateContents
        );
        Article updateArticle = articleRepository.find(ARTICLE_TEST_ID);
        assertThat(updateArticle.getTitle()).isEqualTo(updateTitle);
        assertThat(updateArticle.getCoverUrl()).isEqualTo(updateCoverUrl);
        assertThat(updateArticle.getContents()).isEqualTo(updateContents);
    }
}