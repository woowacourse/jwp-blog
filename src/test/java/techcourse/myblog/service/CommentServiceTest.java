package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import techcourse.myblog.AbstractTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotMatchAuthorException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentServiceTest extends AbstractTest {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

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

    private Long commentId;
    private Comment comment;
    private CommentDto.Response commentDto;


    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(userId)
            .email("email")
            .password("password")
            .name("name")
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

        comment = Comment.builder()
            .contents("good")
            .article(article)
            .user(user)
            .regDate(LocalDateTime.now())
            .modfiedDate(LocalDateTime.now())
            .build();

        userId = userService.save(modelMapper.map(user, UserDto.Create.class));
        userDto = modelMapper.map(user, UserDto.Response.class);
        userDto.setId(userId);

        otherUserId = userService.save(modelMapper.map(otherUser, UserDto.Create.class));
        otherUserDto = modelMapper.map(otherUser, UserDto.Response.class);
        otherUserDto.setId(otherUserId);

        articleId = articleService.save(userDto, modelMapper.map(article, ArticleDto.Create.class));

        CommentDto.Create commentCreateDto = modelMapper.map(comment, CommentDto.Create.class);
        commentCreateDto.setArticleId(articleId);
        commentId = commentService.save(userDto, commentCreateDto);

        commentDto = modelMapper.map(comment, CommentDto.Response.class);
        commentDto.setArticleId(articleId);
    }

    //TODO : MICKITO 미스터코 코드
    //TODO : 시간 변환되서 일단 ID로
    @Test
    void 댓글_전체_조회_테스트() {
        assertThat(commentService.findAllByArticle(articleId)).isEqualTo(Arrays.asList(commentDto));
    }

    @Test
    void 댓글_수정_테스트() {
        Comment updateComment = Comment.builder()
            .contents("updated")
            .article(article)
            .user(user)
            .regDate(LocalDateTime.now())
            .modfiedDate(LocalDateTime.now())
            .build();

        CommentDto.Update updateCommentDto = modelMapper.map(updateComment, CommentDto.Update.class);
        updateCommentDto.setArticleId(articleId);

        CommentDto.Response newCommentDto = commentService.update(userDto, commentId, updateCommentDto);

        assertThat(commentService.findAllByArticle(articleId)).isEqualTo(Arrays.asList(newCommentDto));
    }

    @Test
    void 다른_작성자_댓글_수정_테스트() {
        Comment updateComment = Comment.builder()
            .contents("updated")
            .article(article)
            .user(user)
            .regDate(LocalDateTime.now())
            .modfiedDate(LocalDateTime.now())
            .build();

        CommentDto.Update updateCommentDto = modelMapper.map(updateComment, CommentDto.Update.class);
        assertThrows(NotMatchAuthorException.class, () -> commentService.update(otherUserDto, commentId, updateCommentDto));
    }

    @Test
    void 다른_작성자_삭제_실패_테스트() {
        assertThrows(NotMatchAuthorException.class, () -> commentService.deleteById(otherUserDto, commentId));
    }

    @AfterEach
    void tearDown() {
        commentService.deleteById(userDto, commentId);
        articleService.deleteById(userDto, articleId);
        userService.deleteById(userDto, userId);
        userService.deleteById(otherUserDto, otherUserId);
    }
}