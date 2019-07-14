package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.CouldNotFindArticleIdException;
import techcourse.myblog.web.ArticleDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleRepositoryTest {
    private static final int TEST_ARTICLE_ID = 1;
    private static final int TEST_ARTICLE_ID_NOT_IN_REPO = 2;

    private ArticleRepository articleRepository;
    private ArticleDTO articleDTO;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleDTO = new ArticleDTO(
                "test title",
                "test coverUrl",
                "test contents"
        );

        articleRepository.save(articleDTO);
    }

    @Test
    @DisplayName("게시물을 저장한다.")
    void saveTest() {
        // Given
        ArticleDTO testArticleDTO = new ArticleDTO(
                "saveTest title",
                "saveTest coverUrl",
                "saveTest contents"
        );

        // When
        articleRepository.save(testArticleDTO);

        // Then
        int lastSavedId = articleRepository.getLastGeneratedId();
        Article savedArticle = articleRepository.findBy(lastSavedId);

        assertThat(savedArticle.getTitle()).isEqualTo(testArticleDTO.getTitle());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(testArticleDTO.getCoverUrl());
        assertThat(savedArticle.getContents()).isEqualTo(testArticleDTO.getContents());
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        Article article = articleRepository.findBy(TEST_ARTICLE_ID);

        assertThat(article.getTitle()).isEqualTo(articleDTO.getTitle());
        assertThat(article.getCoverUrl()).isEqualTo(articleDTO.getCoverUrl());
        assertThat(article.getContents()).isEqualTo(articleDTO.getContents());
    }

    @Test
    @DisplayName("수정된 Article을 넘겨받아 해당 update 한다.")
    void updateTest() {
        // Given
        ArticleDTO updatedArticleDTO = new ArticleDTO(
                "updated title",
                "updated coverUrl",
                "updated contents"
        );
        Article updatedArticle = Article.of(TEST_ARTICLE_ID, updatedArticleDTO);

        // When
        articleRepository.updateBy(updatedArticle);

        // Then
        Article resultArticle = articleRepository.findBy(TEST_ARTICLE_ID);
        assertThat(resultArticle.getTitle()).isEqualTo(updatedArticleDTO.getTitle());
        assertThat(resultArticle.getCoverUrl()).isEqualTo(updatedArticleDTO.getCoverUrl());
        assertThat(resultArticle.getContents()).isEqualTo(updatedArticleDTO.getContents());
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.deleteBy(TEST_ARTICLE_ID);
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.findBy(TEST_ARTICLE_ID));
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void findFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.findBy(TEST_ARTICLE_ID_NOT_IN_REPO));
    }

    @Test
    @DisplayName("Repository에 없는 id를 삭제하는 경우 예외를 던져준다.")
    void deleteFailTest() {
        assertThrows(CouldNotFindArticleIdException.class, () -> articleRepository.deleteBy(TEST_ARTICLE_ID_NOT_IN_REPO));
    }

    @Test
    @DisplayName("마지막으로 생성된 article id를 되돌려준다")
    void getLastGeneratedIdTest() {
        assertThat(articleRepository.getLastGeneratedId()).isEqualTo(1);
    }
}