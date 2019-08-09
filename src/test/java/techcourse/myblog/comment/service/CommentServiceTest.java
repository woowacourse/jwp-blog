package techcourse.myblog.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.data.CommentDataForTest;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static techcourse.myblog.data.ArticleDataForTest.*;
import static techcourse.myblog.data.UserDataForTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {
    private User author;
    private Article article;
    private Comment comment;
    private CommentCreateDto commentCreateDto;
    private CommentUpdateDto commentUpdateDto;
    private CommentResponseDto commentResponseDto;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentService commentService;

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

        comment = Comment.builder()
                .contents(CommentDataForTest.COMMENT_CONTENTS)
                .article(article)
                .author(author)
                .build();

        commentCreateDto = modelMapper.map(comment, CommentCreateDto.class);
        commentUpdateDto = modelMapper.map(comment, CommentUpdateDto.class);
        commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);
    }

    @Test
    void 없는게시물_댓글_save() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> commentService.save(article.getId(), author.getId(), commentCreateDto));
    }

    @Test
    void 없는작성자_댓글_save() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(userRepository.findById(article.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> commentService.save(article.getId(), author.getId(), commentCreateDto));
    }

    @Test
    void save() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        when(userRepository.findById(article.getId())).thenReturn(Optional.of(author));
        when(commentRepository.save(commentCreateDto.toComment(author, article))).thenReturn(comment);

        assertThat(commentService.save(article.getId(), author.getId(), commentCreateDto)).isEqualTo(comment);
    }

    @Test
    void findAllByArticleId() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findAllByArticleId(article.getId())).thenReturn(comments);

        assertThat(commentService.findAllByArticleId(article.getId()).get(0).getId()).isEqualTo(commentResponseDto.getId());
        assertThat(commentService.findAllByArticleId(article.getId()).get(0).getContents()).isEqualTo(commentResponseDto.getContents());
        assertThat(commentService.findAllByArticleId(article.getId()).get(0).getAuthor()).isEqualTo(commentResponseDto.getAuthor());
    }

    @Test
    void 없는댓글_update() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundCommentException.class, () -> commentService.update(comment.getId(), author.getId(), commentUpdateDto));
    }

    @Test
    void 잘못된_작성자가_update() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertThrows(NotMatchUserException.class, () -> commentService.update(comment.getId(), 10, commentUpdateDto));
    }

    @Test
    void update() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentResponseDto result = commentService.update(comment.getId(), author.getId(), commentUpdateDto);
        assertThat(result.getAuthor()).isEqualTo(commentResponseDto.getAuthor());
        assertThat(result.getContents()).isEqualTo(commentResponseDto.getContents());
        assertThat(result.getId()).isEqualTo(commentResponseDto.getId());
    }

    @Test
    void 없는댓글_deleteById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundCommentException.class, () -> commentService.deleteById(comment.getId(), author.getId()));
    }

    @Test
    void 맞지않는_작성자_deleteById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertFalse(commentService.deleteById(comment.getId(), 10));
    }

    @Test
    void deleteById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertTrue(commentService.deleteById(comment.getId(), author.getId()));
    }

    @Test
    void 없는댓글_findById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundCommentException.class, () -> commentService.findById(comment.getId()));
    }

    @Test
    void findById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentResponseDto result = commentService.findById(comment.getId());
        assertThat(result.getId()).isEqualTo(commentResponseDto.getId());
        assertThat(result.getContents()).isEqualTo(commentResponseDto.getContents());
        assertThat(result.getAuthor()).isEqualTo(commentResponseDto.getAuthor());
    }
}