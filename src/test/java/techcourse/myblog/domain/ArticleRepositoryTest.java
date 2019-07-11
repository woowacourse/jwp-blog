package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private static final int ARTICLE_TEST_ID = 1;
    private static final int ARTICLE_ID_NOT_IN_REPO = 2;
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article(
                ARTICLE_TEST_ID,
                "test title",
                "test contents",
                "test Url"
        );
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

        assertThrows(IllegalArgumentException.class, () -> articleRepository.find(ARTICLE_ID_NOT_IN_REPO));
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.save(article);

        articleRepository.delete(ARTICLE_TEST_ID);
        assertThrows(IllegalArgumentException.class, () -> articleRepository.find(ARTICLE_TEST_ID));
    }

    @Test
    @DisplayName("게시물의 title을 업데이트 한다.")
    void updateTitleTest() {
        String updatedTitle = "updated title";

        articleRepository.save(article);
        articleRepository.updateTitle(ARTICLE_TEST_ID, updatedTitle);

        Article updatedArticle = articleRepository.find(ARTICLE_TEST_ID);
        assertThat(updatedArticle.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("게시물의 coverUrl을 업데이트 한다.")
    void updateCoverUrlTest() {
        String updateCoverUrl = "updated coverUrl";

        articleRepository.save(article);
        articleRepository.updateCoverUrl(ARTICLE_TEST_ID, updateCoverUrl);

        Article updatedArticle = articleRepository.find(ARTICLE_TEST_ID);
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(updateCoverUrl);
    }

    @Test
    @DisplayName("게시물의 contents를 업데이트 한다.")
    void updateContentsTest() {
        String updateContents = "updated contents";

        articleRepository.save(article);
        articleRepository.updateContents(ARTICLE_TEST_ID, updateContents);

        Article updatedArticle = articleRepository.find(ARTICLE_TEST_ID);
        assertThat(updatedArticle.getContents()).isEqualTo(updateContents);
    }
}