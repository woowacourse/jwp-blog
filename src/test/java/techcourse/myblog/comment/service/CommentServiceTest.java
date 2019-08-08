package techcourse.myblog.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static techcourse.myblog.article.ArticleDataForTest.*;
import static techcourse.myblog.comment.CommentDataForTest.COMMENT_CONTENTS;
import static techcourse.myblog.comment.CommentDataForTest.UPDATED_COMMENT_CONTENTS;
import static techcourse.myblog.user.UserDataForTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {
    private static final long COMMENT_ID = 1;
    private static final long AUTHOR_ID = 1;
    private static final long ARTICLE_ID = 1;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    private User author;
    private Article article;
    private Comment comment;

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

        comment = Comment.builder()
                .id(COMMENT_ID)
                .contents(COMMENT_CONTENTS)
                .author(author)
                .article(article)
                .build();
    }

    @Test
    void 댓글_조회_테스트() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));

        assertThat(commentService.find(COMMENT_ID)).isEqualTo(modelMapper.map(comment, CommentResponseDto.class));
    }

    @Test
    void 댓글_조회_시_없는_댓글인_경우_예외처리() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundCommentException.class, () -> commentService.find(COMMENT_ID));
    }

    @Test
    void 댓글_생성_테스트() {
        CommentCreateDto commentCreateDto = modelMapper.map(comment, CommentCreateDto.class);

        given(userRepository.findById(AUTHOR_ID)).willReturn(Optional.of(author));
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));
        given(commentRepository.save(commentCreateDto.toComment(author, article))).willReturn(comment);

        assertThat(commentService.save(ARTICLE_ID, AUTHOR_ID, commentCreateDto))
                .isEqualTo(modelMapper.map(comment, CommentResponseDto.class));
    }

    @Test
    void 댓글_생성_시_없는_회원인_경우_예외처리() {
        CommentCreateDto commentCreateDto = modelMapper.map(comment, CommentCreateDto.class);

        given(userRepository.findById(AUTHOR_ID)).willReturn(Optional.empty());
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        assertThrows(NotFoundUserException.class, () -> commentService.save(ARTICLE_ID, AUTHOR_ID, commentCreateDto));
    }

    @Test
    void 댓글_생성_시_없는_게시글인_경우_예외처리() {
        CommentCreateDto commentCreateDto = modelMapper.map(comment, CommentCreateDto.class);

        given(userRepository.findById(AUTHOR_ID)).willReturn(Optional.of(author));
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundArticleException.class, () -> commentService.save(ARTICLE_ID, AUTHOR_ID, commentCreateDto));
    }

    @Test
    void 댓글_업데이트_테스트() {
        Comment updateComment = Comment.builder()
                .id(COMMENT_ID)
                .contents(UPDATED_COMMENT_CONTENTS)
                .author(author)
                .article(article)
                .build();

        CommentUpdateDto commentUpdateDto = modelMapper.map(updateComment, CommentUpdateDto.class);

        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));

        assertThat(commentService.update(COMMENT_ID, AUTHOR_ID, commentUpdateDto))
                .isEqualTo(modelMapper.map(updateComment, CommentResponseDto.class));
    }

    @Test
    void 댓글_업데이트_시_없는_댓글인_경우_예외처리() {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto();

        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.empty());

        assertThrows(NotFoundCommentException.class, () -> commentService.update(COMMENT_ID, AUTHOR_ID, commentUpdateDto));
    }

    @Test
    void 댓글_업데이트_시_본인의_댓글이_아닌_경우_예외처리() {
        long nonAuthorId = 0;
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto();

        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));

        assertThrows(NotMatchUserException.class, () -> commentService.update(COMMENT_ID, nonAuthorId, commentUpdateDto));
    }

    @Test
    void 댓글_삭제_테스트() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));
        willDoNothing().given(commentRepository).delete(comment);

        assertDoesNotThrow(() -> commentService.delete(COMMENT_ID, AUTHOR_ID));
    }

    @Test
    void 댓글_삭제_시_없는_댓글인_경우_예외처리() {
        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.empty());
        willDoNothing().given(commentRepository).delete(comment);

        assertThrows(NotFoundCommentException.class, () -> commentService.delete(COMMENT_ID, AUTHOR_ID));
    }

    @Test
    void 댓글_삭제_시_본인의_댓글이_아닌_경우_예외처리() {
        long nonAuthorId = 0;

        given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(comment));
        willDoNothing().given(commentRepository).delete(comment);

        assertThrows(NotMatchUserException.class, () -> commentService.delete(COMMENT_ID, nonAuthorId));
    }
}
