package techcourse.myblog.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.dto.CommentDto;
import techcourse.myblog.user.UserDataForTest;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private User author;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .email(UserDataForTest.USER_EMAIL)
                .name(UserDataForTest.USER_NAME)
                .password(UserDataForTest.USER_PASSWORD)
                .build();
        author = userRepository.save(author);

        article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .author(author)
                .build();
        article = articleRepository.save(article);

        comment = Comment.builder()
                .contents("comment")
                .author(author)
                .article(article)
                .build();

        comment = commentService.save(
                article.getId(), author.getId(), modelMapper.map(comment, CommentDto.Creation.class));
    }

    @Test
    void 댓글_조회_테스트() {
        assertThat(commentService.findById(comment.getId())).isEqualTo(modelMapper.map(comment, CommentDto.Response.class));
    }

    @Test
    void 댓글_업데이트_테스트() {
        Comment updateComment = Comment.builder()
                .contents("updateContents")
                .author(author)
                .article(article)
                .build();
        commentService.update(comment.getId(), author.getId(), modelMapper.map(updateComment, CommentDto.Updation.class));
        assertThat(commentService.findById(comment.getId()).getContents())
                .isEqualTo(modelMapper.map(updateComment, CommentDto.Response.class).getContents());
    }

    @AfterEach
    void tearDown() {
        commentService.delete(comment.getId(), author.getId());
    }
}
