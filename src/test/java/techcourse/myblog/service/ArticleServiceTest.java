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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ArticleServiceTest extends AbstractTest {
    private static Long articleId = Long.valueOf(1);
    private static Long userId;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private Article article;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                        .email("aaa@gamil.com")
                        .name("asd")
                        .password("123qwe!@#")
                        .build();

        //TODO : User 저장
        userId = userService.save(modelMapper.map(user, UserDto.Create.class));

        article = Article.builder()
                .id(articleId)
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        UserDto.Response userDto = modelMapper.map(user, UserDto.Response.class);
        userDto.setId(userId);

        articleService.save(userDto,
            modelMapper.map(article, ArticleDto.Create.class));
    }

    @Test
    void 게시글_전체_조회_테스트() {
        assertThat(articleService.findAll()).isEqualTo(
                Arrays.asList(modelMapper.map(article, ArticleDto.Response.class)));
    }

    @Test
    void 게시글_단건_조회_테스트() {
        assertThat(articleService.findById(articleId)).isEqualTo(modelMapper.map(article, ArticleDto.Response.class));
    }

    @Test
    void 게시글_수정_테스트() {
        Article updatedArticle = Article.builder()
                .id(articleId)
                .title("updatedTitle")
                .coverUrl("updatedCoverUrl")
                .contents("updatedContents")
                .build();

        long updatedArticleId = articleService.update(modelMapper.map(user, UserDto.Response.class),
            articleId, modelMapper.map(updatedArticle, ArticleDto.Update.class));

        assertThat(articleService.findById(updatedArticleId))
            .isEqualTo(modelMapper.map(updatedArticle, ArticleDto.Response.class));
    }

    @AfterEach
    void tearDown() {
        articleService.deleteById(modelMapper.map(user, UserDto.Response.class),
            articleId++);
        userService.deleteById(userId);
    }
}