package techcourse.myblog.article.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.dto.ArticleDto;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Article article;
    private User author;
    private long articleId;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .email(UserDataForTest.USER_EMAIL)
                .name(UserDataForTest.USER_NAME)
                .password(UserDataForTest.USER_PASSWORD)
                .build();

        article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .author(author)
                .build();

        User savedAuthor = userRepository.save(author);

        article = articleService.save(modelMapper.map(article, ArticleDto.Creation.class), savedAuthor.getId());
        articleId = article.getId();
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
        Article updatedUser = Article.builder()
                .id(articleId)
                .title("updatedTitle")
                .coverUrl("updatedCoverUrl")
                .contents("updatedContents")
                .author(author)
                .build();

        long updatedArticleId = articleService.update(
                articleId, modelMapper.map(updatedUser, ArticleDto.Updation.class), author.getId());
        assertThat(articleService.findById(updatedArticleId))
                .isEqualTo(modelMapper.map(updatedUser, ArticleDto.Response.class));
    }

    @AfterEach
    void tearDown() {
        articleService.deleteById(articleId++, author.getId());
        userRepository.deleteAll();
    }
}