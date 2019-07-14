package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.CouldNotFindArticleIdException;
import techcourse.myblog.web.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest {
    private static final int TEST_ARTICLE_ID = 1;
    private static final int TEST_ARTICLE_ID_NOT_IN_REPO = 2;

    private ArticleRepository articleRepository;
    private ArticleDto articleDto;

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();
        articleDto = new ArticleDto(
                "test title",
                "test coverUrl",
                "test contents"
        );

        articleRepository.save(articleDto);
    }

    @Test
    @DisplayName("게시물을 저장한다.")
    void saveTest() {
        // Given
        ArticleDto testArticleDto = new ArticleDto(
                "saveTest title",
                "saveTest coverUrl",
                "saveTest contents"
        );

        // When
        articleRepository.save(testArticleDto);

        // Then
        int lastSavedId = articleRepository.getLastGeneratedId();
        Article savedArticle = articleRepository.findBy(lastSavedId);

        assertThat(savedArticle.getTitle()).isEqualTo(testArticleDto.getTitle());
        assertThat(savedArticle.getCoverUrl()).isEqualTo(testArticleDto.getCoverUrl());
        assertThat(savedArticle.getContents()).isEqualTo(testArticleDto.getContents());
    }

    @Test
    @DisplayName("게시물 id로 게시물을 조회한다.")
    void findTest() {
        Article article = articleRepository.findBy(TEST_ARTICLE_ID);

        assertThat(article.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(article.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
        assertThat(article.getContents()).isEqualTo(articleDto.getContents());
    }

    @Test
    @DisplayName("수정된 Article을 넘겨받아 해당 update 한다.")
    void updateTest() {
        // Given
        ArticleDto updatedArticleDto = new ArticleDto(
                "updated title",
                "updated coverUrl",
                "updated contents"
        );

        // When
        articleRepository.updateBy(TEST_ARTICLE_ID, updatedArticleDto);

        // Then
        Article resultArticle = articleRepository.findBy(TEST_ARTICLE_ID);

        assertThat(resultArticle.getTitle()).isEqualTo(updatedArticleDto.getTitle());
        assertThat(resultArticle.getCoverUrl()).isEqualTo(updatedArticleDto.getCoverUrl());
        assertThat(resultArticle.getContents()).isEqualTo(updatedArticleDto.getContents());
    }

    @Test
    @DisplayName("게시물 id를 이용해 해당 게시물을 지운다.")
    void deleteByIdTest() {
        articleRepository.deleteBy(TEST_ARTICLE_ID);
        assertThatThrownBy(() -> articleRepository.findBy(TEST_ARTICLE_ID))
                .isInstanceOf(CouldNotFindArticleIdException.class);
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void findFailTest() {
        assertThatThrownBy(() -> articleRepository.findBy(TEST_ARTICLE_ID_NOT_IN_REPO))
                .isInstanceOf(CouldNotFindArticleIdException.class);
    }

    @Test
    @DisplayName("Repository에 없는 id를 삭제하는 경우 예외를 던져준다.")
    void deleteFailTest() {
        assertThatThrownBy(() -> articleRepository.deleteBy(TEST_ARTICLE_ID_NOT_IN_REPO)).
                isInstanceOf(CouldNotFindArticleIdException.class);
    }

    @Test
    @DisplayName("마지막으로 생성된 article id를 되돌려준다")
    void getLastGeneratedIdTest() {
        assertThat(articleRepository.getLastGeneratedId()).isEqualTo(1);
    }
}