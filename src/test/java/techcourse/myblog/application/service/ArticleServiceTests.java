package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ArticleServiceTests {
    private static final String EXIST_EMAIL = "zino@naver.com";
    private static final String NOT_EXIST_EMAIL = "zino1@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "zinozino";
    private static final Long EXIST_ARTICLE_ID = 1L;
    private static final Long NOT_EXIST_ARTICLE_ID = 2L;
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "cover_url";

    private ArticleService articleService;
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    private ArticleServiceTests() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
        articleService = new ArticleService(articleRepository, userService);
        initUserRepositoryMock();
        initArticleRepositoryMock();
    }

    private void initUserRepositoryMock() {
        User existUser = new User(EXIST_EMAIL, NAME, PASSWORD);
        User notExistUser = new User(NOT_EXIST_EMAIL, NAME, PASSWORD);

        when(userRepository.findByEmail(EXIST_EMAIL)).thenReturn(Optional.of(existUser));
        when(userRepository.findByEmail(NOT_EXIST_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(notExistUser)).thenReturn(notExistUser);
        Mockito.doNothing().when(userRepository).deleteByEmail(EXIST_EMAIL);
    }

    private void initArticleRepositoryMock() {
        Article articleBeforeSave = new Article.ArticleBuilder()
                .author(userRepository.findByEmail(EXIST_EMAIL).get())
                .coverUrl(COVER_URL)
                .title(TITLE)
                .contents(CONTENTS).build();
        Article articleAfterSave = new Article(EXIST_ARTICLE_ID, TITLE, COVER_URL, CONTENTS, userRepository.findByEmail(EXIST_EMAIL).get());
        when(articleRepository.findById(EXIST_ARTICLE_ID)).thenReturn(Optional.of(articleAfterSave));
        when(articleRepository.findById(NOT_EXIST_ARTICLE_ID)).thenReturn(Optional.empty());
        when(articleRepository.save(articleBeforeSave)).thenReturn(articleAfterSave);
        Mockito.doNothing().when(articleRepository).deleteById(EXIST_ARTICLE_ID);
        when(articleRepository.findAll()).thenReturn(Collections.singletonList(articleAfterSave));
    }

    @Test
    void Article_생성_테스트() {
        ArticleDto articleDto = new ArticleDto(NOT_EXIST_ARTICLE_ID, TITLE, COVER_URL, CONTENTS, userRepository.findByEmail(EXIST_EMAIL).get());
        assertDoesNotThrow(() -> articleService.save(articleDto, EXIST_EMAIL));
    }

    @Test
    void 존재하는_Article_조회_테스트() {
        Article article = articleService.findById(EXIST_ARTICLE_ID);

        assertThat(article.getId()).isEqualTo(EXIST_ARTICLE_ID);
        assertThat(article.getTitle()).isEqualTo(TITLE);
        assertThat(article.getCoverUrl()).isEqualTo(COVER_URL);
        assertThat(article.getContents()).isEqualTo(CONTENTS);
        assertThat(article.getAuthor().getEmail()).isEqualTo(userRepository.findByEmail(EXIST_EMAIL).get().getEmail());
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
        assertThat(articleDto.getAuthor().getEmail()).isEqualTo(userRepository.findByEmail(EXIST_EMAIL).get().getEmail());
    }

    @Test
    void 글작성자와_세션이_같을때_비교_테스트() {
        assertDoesNotThrow(() -> articleService.checkAuthor(EXIST_ARTICLE_ID, EXIST_EMAIL));
    }

    @Test
    void 글작성자와_세션이_다를때_비교_테스트() {
        assertThrows(NotMatchAuthorException.class, () -> articleService.checkAuthor(EXIST_ARTICLE_ID, NOT_EXIST_EMAIL));
    }
}