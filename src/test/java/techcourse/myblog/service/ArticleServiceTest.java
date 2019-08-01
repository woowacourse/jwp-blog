package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import techcourse.myblog.AbstractTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotMatchAuthorException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleServiceTest extends AbstractTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private Long userId;
    private User user;
    private UserDto.Response userDto;

    private Long otherUserId;
    private User otherUser;
    private UserDto.Response otherUserDto;

    private Long articleId;
    private Article article;
    private ArticleDto.Response articleDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .email("aaa@gamil.com")
            .name("asd")
            .password("123qwe!@#")
            .build();

        otherUser = User.builder()
            .email("bbb@gamil.com")
            .name("asd")
            .password("123qwe!@#")
            .build();

        article = Article.builder()
            .title("title")
            .coverUrl("coverUrl")
            .contents("contents")
            .author(user)
            .build();

        userId = userService.save(modelMapper.map(user, UserDto.Create.class));
        userDto = modelMapper.map(user, UserDto.Response.class);
        userDto.setId(userId);

        otherUserId = userService.save(modelMapper.map(otherUser, UserDto.Create.class));
        otherUserDto = modelMapper.map(otherUser, UserDto.Response.class);
        otherUserDto.setId(otherUserId);

        articleId = articleService.save(userDto, modelMapper.map(article, ArticleDto.Create.class));
        articleDto = modelMapper.map(article, ArticleDto.Response.class);
        articleDto.setId(articleId);
    }

    //TODO : ArticleDto Equals가 테스트 코드를 위해 존재..?
    @Test
    void 게시글_전체_조회_테스트() {
        assertThat(articleService.findAll()).isEqualTo(Arrays.asList(articleDto));
    }

    @Test
    void 게시글_단건_조회_테스트() {
        assertThat(articleService.findById(articleId)).isEqualTo(articleDto);
    }

    @Test
    void 작석자_확인_게시글_단건_조회_테스트() {
        assertThat(articleService.findById(userDto, articleId)).isEqualTo(articleDto);
    }

    @Test
    void 다른_작석자_확인_게시글_단건_조회_테스트() {
        assertThrows(NotMatchAuthorException.class, () ->
            articleService.findById(otherUserDto, articleId));
    }

    @Test
    void 게시글_수정_테스트() {
        Article updatedArticle = Article.builder()
            .id(articleId)
            .title("updatedTitle")
            .coverUrl("updatedCoverUrl")
            .contents("updatedContents")
            .author(user)
            .build();

        long updatedArticleId = articleService.update(userDto, articleId, modelMapper.map(updatedArticle, ArticleDto.Update.class));

        assertThat(articleService.findById(updatedArticleId))
            .isEqualTo(modelMapper.map(updatedArticle, ArticleDto.Response.class));
    }

    @Test
    void 다른_작성자_게시글_수정_테스트() {
        Article updatedArticle = Article.builder()
            .id(articleId)
            .title("updatedTitle")
            .coverUrl("updatedCoverUrl")
            .contents("updatedContents")
            .author(user)
            .build();

        assertThrows(NotMatchAuthorException.class, () ->
            articleService.update(otherUserDto, articleId, modelMapper.map(updatedArticle, ArticleDto.Update.class)));
    }

    @Test
    void 다른_작성자_게시글_삭제_테스트() {
        assertThrows(NotMatchAuthorException.class, () ->
            articleService.deleteById(otherUserDto, articleId));
    }

    @AfterEach
    void tearDown() {
        userService.deleteById(otherUserDto, otherUserId);
        articleService.deleteById(userDto, articleId);
        userService.deleteById(userDto, userId);
    }
}