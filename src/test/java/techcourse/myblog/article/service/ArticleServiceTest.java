package techcourse.myblog.article.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.dto.ArticleCreateDto;
import techcourse.myblog.article.dto.ArticleResponseDto;
import techcourse.myblog.article.dto.ArticleUpdateDto;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static techcourse.myblog.article.ArticleDataForTest.*;
import static techcourse.myblog.user.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    private static final long ARTICLE_ID = 1;
    private static final long AUTHOR_ID = 1;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(AUTHOR_ID)
                .email(USER_EMAIL)
                .name(USER_NAME)
                .password(USER_PASSWORD)
                .build();

        article = Article.builder()
                .id(ARTICLE_ID)
                .title(ARTICLE_TITLE)
                .coverUrl(ARTICLE_COVER_URL)
                .contents(ARTICLE_CONTENTS)
                .author(author)
                .build();
    }

    @Test
    void 게시글_저장_테스트() {
        ArticleCreateDto articleCreateDto = modelMapper.map(article, ArticleCreateDto.class);

        given(userRepository.findById(AUTHOR_ID)).willReturn(Optional.of(author));
        given(articleRepository.save(articleCreateDto.toArticle(author))).willReturn(article);

        assertThat(articleService.save(articleCreateDto, AUTHOR_ID))
                .isEqualTo(modelMapper.map(article, ArticleResponseDto.class));
    }

    @Test
    void 게시글_저장_시_없는_없는_유저일_경우_예외처리() {
        ArticleCreateDto articleCreateDto = modelMapper.map(article, ArticleCreateDto.class);

        given(userRepository.findById(AUTHOR_ID)).willReturn(Optional.empty());
        given(articleRepository.save(articleCreateDto.toArticle(author))).willReturn(article);

        assertThrows(NotFoundUserException.class, () -> articleService.save(articleCreateDto, AUTHOR_ID));
    }

    @Test
    void 게시글_전체_조회_테스트() {
        given(articleRepository.findAll()).willReturn(Arrays.asList(article));

        assertThat(articleService.findAll()).isEqualTo(
                Arrays.asList(modelMapper.map(article, ArticleResponseDto.class)));
    }

    @Test
    void 게시글_단건_조회_테스트() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        assertThat(articleService.find(ARTICLE_ID, AUTHOR_ID)).isEqualTo(modelMapper.map(article, ArticleResponseDto.class));
    }

    @Test
    void 게시글_단건_조회_시_예외처리() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> articleService.find(ARTICLE_ID, AUTHOR_ID));
    }

    @Test
    void 게시글_수정_테스트() {
        Article updatedUser = Article.builder()
                .id(ARTICLE_ID)
                .title(UPDATE_TITLE)
                .coverUrl(UPDATE_COVER_URL)
                .contents(UPDATE_CONTENTS)
                .author(author)
                .build();

        ArticleUpdateDto articleUpdateDto = modelMapper.map(updatedUser, ArticleUpdateDto.class);

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        assertThat(articleService.update(ARTICLE_ID, articleUpdateDto, AUTHOR_ID))
                .isEqualTo(modelMapper.map(updatedUser, ArticleResponseDto.class));
    }

    @Test
    void 게시글_수정_시_없는_게시물인_경우_예외처리() {
        ArticleUpdateDto empty = new ArticleUpdateDto();

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> articleService.update(ARTICLE_ID, empty, AUTHOR_ID));
    }

    @Test
    void 게시글_수정_시_자신의_게시물이_아닌_경우_예외처리() {
        long nonAuthorId = 0;
        ArticleUpdateDto empty = new ArticleUpdateDto();

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        assertThrows(NotMatchUserException.class, () -> articleService.update(ARTICLE_ID, empty, nonAuthorId));
    }

    @Test
    void 게시글_삭제_테스트() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));
        willDoNothing().given(articleRepository).delete(article);

        assertDoesNotThrow(() -> articleService.deleteById(ARTICLE_ID, AUTHOR_ID));
    }

    @Test
    void 게시글_삭제_시_없는_게시글인_경우_예외처리() {
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> articleService.deleteById(ARTICLE_ID, AUTHOR_ID));
    }

    @Test
    void 게시글_삭제_시_자신의_게시물이_아닌_경우_예외처리() {
        long nonAuthorId = 0;

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        assertThrows(NotMatchUserException.class, () -> articleService.deleteById(ARTICLE_ID, nonAuthorId));
    }
}