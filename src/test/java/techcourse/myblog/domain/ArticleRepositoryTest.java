package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.validator.CouldNotFindArticleIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private static final int TEST_ARTICLE_ID = 1;
    private static final int TEST_ARTICLE_ID_NOT_IN_REPO = 2;

    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        article = new Article(
                TEST_ARTICLE_ID,
                "test title",
                "test coverUrl",
                "test contents"
        );

        articleRepository.save(article);
    }

    @Test
    @DisplayName("게시물을 저장한다.")
    void saveTest() {
        Article savingArticle = new Article(
                0,
                "saveTest title",
                "saveTest coverUrl",
                "saveTest contents"
        );

        articleRepository.save(savingArticle);

        assertThat(articleRepository.findAll()).contains(savingArticle);
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        assertThat(articleRepository.find(TEST_ARTICLE_ID)).isEqualTo(article);
    }

    @Test
    @DisplayName("게시물의 title을 업데이트 한다.")
    void updateTitleTest() {
        String updatedTitle = "updated title";

        articleRepository.updateTitle(TEST_ARTICLE_ID, updatedTitle);

        Article updatedArticle = articleRepository.find(TEST_ARTICLE_ID);
        assertThat(updatedArticle.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @DisplayName("게시물의 coverUrl을 업데이트 한다.")
    void updateCoverUrlTest() {
        String updateCoverUrl = "updated coverUrl";

        articleRepository.updateCoverUrl(TEST_ARTICLE_ID, updateCoverUrl);

        Article updatedArticle = articleRepository.find(TEST_ARTICLE_ID);
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(updateCoverUrl);
    }

    @Test
    @DisplayName("게시물의 contents를 업데이트 한다.")
    void updateContentsTest() {
        String updateContents = "updated contents";

        articleRepository.updateContents(TEST_ARTICLE_ID, updateContents);

        Article updatedArticle = articleRepository.find(TEST_ARTICLE_ID);
        assertThat(updatedArticle.getContents()).isEqualTo(updateContents);
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.delete(TEST_ARTICLE_ID);
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.find(TEST_ARTICLE_ID));
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void findFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.find(TEST_ARTICLE_ID_NOT_IN_REPO));
    }

    @Test
    @DisplayName("Repository에 없는 id의 title을 수정 시도하는 경우 예외를 던져준다.")
    void updateTitleFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.updateContents(
                TEST_ARTICLE_ID_NOT_IN_REPO, "new contents"
        ));
    }

    @Test
    @DisplayName("Repository에 없는 id의 coverUrl을 수정 시도하는 경우 예외를 던져준다.")
    void updateCoverUrlFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.updateContents(
                TEST_ARTICLE_ID_NOT_IN_REPO, "new contents"
        ));
    }

    @Test
    @DisplayName("Repository에 없는 id의 contents를 수정 시도하는 경우 예외를 던져준다.")
    void updateContentsFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.updateContents(
                TEST_ARTICLE_ID_NOT_IN_REPO, "new contents"
        ));
    }

    @Test
    @DisplayName("Repository에 없는 id를 삭제하는 경우 예외를 던져준다.")
    void deleteFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.delete(TEST_ARTICLE_ID_NOT_IN_REPO));
    }
}