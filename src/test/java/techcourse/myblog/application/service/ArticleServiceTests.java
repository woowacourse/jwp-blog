package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.assembler.UserAssembler;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchArticleAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ArticleServiceTests {
    private static final String EXIST_EMAIL = "zino@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "zinozino";
    private static final Long EXIST_ARTICLE_ID = 1L;
    private static final Long NOT_EXIST_ARTICLE_ID = 2L;
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "cover_url";

    private ArticleService articleService;
    private UserAssembler userAssembler = UserAssembler.getInstance();

    @Mock
    private UserService userService;
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private User firstUser;
    @Mock
    private User secondUser;

    private ArticleServiceTests() {
        MockitoAnnotations.initMocks(this);
        articleService = new ArticleService(articleRepository, userService);
        initUserMock();
        initUserServiceMock();
        initArticleRepositoryMock();
    }

    private void initUserMock() {
        when(firstUser.getEmail()).thenReturn(EXIST_EMAIL);
        when(firstUser.getId()).thenReturn(1L);
        when(firstUser.getName()).thenReturn(NAME);
        when(firstUser.getPassword()).thenReturn(PASSWORD);
        when(secondUser.getEmail()).thenReturn(EXIST_EMAIL + "c");
        when(secondUser.getId()).thenReturn(2L);
        when(secondUser.getName()).thenReturn(NAME + "c");
        when(secondUser.getPassword()).thenReturn(PASSWORD + "c");
    }

    private void initUserServiceMock() {
        when(userService.findUserById(EXIST_ARTICLE_ID)).thenReturn(new User(EXIST_EMAIL, NAME, PASSWORD));
    }

    private void initArticleRepositoryMock() {
        Article articleBeforeSave = new Article.ArticleBuilder()
                .coverUrl(COVER_URL)
                .title(TITLE)
                .contents(CONTENTS)
                .author(firstUser)
                .build();
        Article articleAfterSave = new Article(EXIST_ARTICLE_ID, TITLE, COVER_URL, CONTENTS, firstUser);
        when(articleRepository.findById(EXIST_ARTICLE_ID)).thenReturn(Optional.of(articleAfterSave));
        when(articleRepository.findById(NOT_EXIST_ARTICLE_ID)).thenReturn(Optional.empty());
        when(articleRepository.save(articleBeforeSave)).thenReturn(articleAfterSave);
        Mockito.doNothing().when(articleRepository).deleteById(EXIST_ARTICLE_ID);
        when(articleRepository.findAll()).thenReturn(Collections.singletonList(articleAfterSave));
    }

    @Test
    void Article_생성_테스트() {
        ArticleDto articleDto = new ArticleDto(NOT_EXIST_ARTICLE_ID
                , TITLE
                , COVER_URL
                , CONTENTS
                , userAssembler.convertEntityToDto(firstUser));
        assertDoesNotThrow(() -> articleService.save(articleDto, firstUser));
    }

    @Test
    void 존재하는_Article_조회_테스트() {
        ArticleDto article = articleService.findById(EXIST_ARTICLE_ID);

        assertThat(article.getId()).isEqualTo(EXIST_ARTICLE_ID);
        assertThat(article.getTitle()).isEqualTo(TITLE);
        assertThat(article.getCoverUrl()).isEqualTo(COVER_URL);
        assertThat(article.getContents()).isEqualTo(CONTENTS);
        assertThat(article.getAuthor().getEmail()).isEqualTo(firstUser.getEmail());
    }

    @Test
    void 존재하지않는_Article_조회_테스트() {
        assertThrows(NotExistArticleIdException.class, () -> articleService.findById(NOT_EXIST_ARTICLE_ID));
    }

    @Test
    void Article_삭제_테스트() {
        assertDoesNotThrow(() -> articleService.removeById(EXIST_ARTICLE_ID));
    }

    @Test
    void 전체_Article_조회_테스트() {
        List<ArticleDto> articleDtos = articleService.findAll();
        assertThat(articleDtos).hasSize(1);

        ArticleDto articleDto = articleDtos.get(0);
        assertThat(articleDto.getId()).isEqualTo(EXIST_ARTICLE_ID);
        assertThat(articleDto.getTitle()).isEqualTo(TITLE);
        assertThat(articleDto.getCoverUrl()).isEqualTo(COVER_URL);
        assertThat(articleDto.getContents()).isEqualTo(CONTENTS);
        assertThat(articleDto.getAuthor().getEmail()).isEqualTo(firstUser.getEmail());
    }

    @Test
    void 글작성자와_세션이_같을때_비교_테스트() {
        assertDoesNotThrow(() -> articleService.matchAuthor(EXIST_ARTICLE_ID, firstUser));
    }

    @Test
    void 글작성자와_세션이_다를때_비교_테스트() {
        assertThrows(NotMatchArticleAuthorException.class, () -> articleService.matchAuthor(EXIST_ARTICLE_ID, secondUser));
    }
}