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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static techcourse.myblog.data.ArticleDataForTest.*;
import static techcourse.myblog.data.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    private User author;
    private Article article;
    private ArticleCreateDto articleCreateDto;
    private ArticleUpdateDto articleUpdateDto;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(1)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .name(USER_NAME)
                .build();

        article = Article.builder()
                .author(author)
                .title(ARTICLE_TITLE)
                .coverUrl(ARTICLE_COVER_URL)
                .contents(ARTICLE_CONTENTS)
                .id(1)
                .build();

        articleCreateDto = modelMapper.map(article, ArticleCreateDto.class);

        articleUpdateDto = modelMapper.map(article, ArticleUpdateDto.class);
    }

    @Test
    void findAll() {
        List<Article> articles = Arrays.asList(article);
        when(articleRepository.findAll()).thenReturn(articles);

        assertThat(articleService.findAll().get(0).getId()).isEqualTo(article.getId());
        assertThat(articleService.findAll().get(0).getTitle()).isEqualTo(article.getTitle());
        assertThat(articleService.findAll().get(0).getCoverUrl()).isEqualTo(article.getCoverUrl());
        assertThat(articleService.findAll().get(0).getContents()).isEqualTo(article.getContents());
        assertThat(articleService.findAll().get(0).getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    void 유저아이디가_없는_save() {
        when(userRepository.findById(author.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> articleService.save(articleCreateDto, author.getId()));
    }

    @Test
    void save() {
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(articleRepository.save(articleCreateDto.toArticle(author))).thenReturn(article);

        assertThat(articleService.save(articleCreateDto, author.getId())).isEqualTo(author.getId());
    }

    @Test
    void 없는게시물_findById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> articleService.findById(article.getId()));
    }

    @Test
    void findById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        ArticleResponseDto result = articleService.findById(article.getId());
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getTitle()).isEqualTo(article.getTitle());
        assertThat(result.getCoverUrl()).isEqualTo(article.getCoverUrl());
        assertThat(result.getContents()).isEqualTo(article.getContents());
        assertThat(result.getId()).isEqualTo(article.getId());
    }

    @Test
    void 없는게시물_update() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> articleService.update(article.getId(), articleUpdateDto, author.getId()));
    }

    @Test
    void 자기게시물_아닌_update() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        assertThrows(NotMatchUserException.class, () -> articleService.update(article.getId(), articleUpdateDto, 10));
    }

    @Test
    void update() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        ArticleResponseDto result = articleService.update(article.getId(), articleUpdateDto, author.getId());
        assertThat(result.getId()).isEqualTo(article.getId());
        assertThat(result.getContents()).isEqualTo(article.getContents());
        assertThat(result.getCoverUrl()).isEqualTo(article.getCoverUrl());
        assertThat(result.getTitle()).isEqualTo(article.getTitle());
        assertThat(result.getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    void 없는게시물_deleteById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());
        doNothing().when(articleRepository).deleteById(article.getId());

        assertThrows(NotFoundArticleException.class, () -> articleService.deleteById(article.getId(), author.getId()));
    }

    @Test
    void 자기게시물_아닌_deleteById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        assertFalse(articleService.deleteById(article.getId(), 10));
    }

    @Test
    void deleteById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        assertTrue(articleService.deleteById(article.getId(), author.getId()));
    }
}
