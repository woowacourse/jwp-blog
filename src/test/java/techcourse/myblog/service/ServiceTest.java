package techcourse.myblog.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleVo;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class ServiceTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected ArticleService articleService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected ModelMapper modelMapper;

    protected Long userId;
    protected User user;
    protected UserDto.Response userDto;

    protected Long otherUserId;
    protected User otherUser;
    protected UserDto.Response otherUserDto;

    protected Long articleId;
    protected Article article;
    protected ArticleDto.Response articleDto;

    protected Long commentId;
    protected Comment comment;
    protected CommentDto.Response commentDto;

    protected void init() {
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
            .articleVo(new ArticleVo("title", "coverUrl", "contents"))
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

        articleId = articleService.save(userDto, modelMapper.map(article, ArticleVo.class));
        articleDto = modelMapper.map(article, ArticleDto.Response.class);
        articleDto.setId(articleId);

        CommentDto.Create commentCreateDto = modelMapper.map(comment, CommentDto.Create.class);
        commentCreateDto.setArticleId(articleId);
        commentId = commentService.save(userDto, commentCreateDto);

        commentDto = modelMapper.map(comment, CommentDto.Response.class);
        commentDto.setArticleId(articleId);
    }

    protected void terminate(){
        commentService.deleteById(userDto, commentId);
        articleService.deleteById(userDto, articleId);
        userService.deleteById(userDto, userId);
        userService.deleteById(otherUserDto, otherUserId);
    }
}
