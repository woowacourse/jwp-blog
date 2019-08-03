package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.assembler.ArticleAssembler;
import techcourse.myblog.application.assembler.UserAssembler;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.CommentNotFoundException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CommentServiceTests {
    private static final String FIRST_EMAIL = "zino@naver.com";
    private static final String SECOND_EMAIL = "zino1@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "zinozino";
    private static final Long EXIST_ARTICLE_ID = 1L;
    private static final String TITLE = "title";
    private static final String CONTENTS = "contents";
    private static final String COVER_URL = "cover_url";

    private CommentService commentService;

    private ArticleAssembler articleAssembler = ArticleAssembler.getInstance();
    private UserAssembler userAssembler = UserAssembler.getInstance();

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleService articleService;

    @Mock
    private UserService userService;

    public CommentServiceTests() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository, userService, articleService);
        initUserServiceMock();
        initArticleServiceMock();
        initCommentRepository();
    }

    private void initUserServiceMock() {
        when(userService.findUserById(1L)).thenReturn(new User(1L, FIRST_EMAIL, NAME, PASSWORD));
        when(userService.findUserById(2L)).thenReturn(new User(2L, SECOND_EMAIL, NAME, PASSWORD));
    }

    private void initArticleServiceMock() {
        User user = userService.findUserById(1L);
        ArticleDto articleDto = new ArticleDto(EXIST_ARTICLE_ID
                , TITLE
                , COVER_URL
                , CONTENTS
                , userAssembler.convertEntityToDto(user));
        when(articleService.findById(EXIST_ARTICLE_ID)).thenReturn(articleDto);
        when(articleService.findArticleById(EXIST_ARTICLE_ID))
                .thenReturn(new Article(EXIST_ARTICLE_ID, TITLE, COVER_URL, CONTENTS, user));
    }

    private void initCommentRepository() {
        Article article = articleService.findArticleById(EXIST_ARTICLE_ID);
        Comment firstComment = new Comment(1L, CONTENTS, userService.findUserById(1L), article);
        Comment secondComment = new Comment(2L, CONTENTS, userService.findUserById(2L), article);
        when(commentRepository.save(firstComment)).thenReturn(firstComment);
        when(commentRepository.findByArticle(article)).thenReturn(Arrays.asList(firstComment, secondComment));
        Mockito.doNothing().when(commentRepository).deleteById(1L);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(firstComment));
        when(commentRepository.findById(2L)).thenReturn(Optional.of(secondComment));
        when(commentRepository.findById(3L)).thenReturn(Optional.empty());
    }

    @Test
    void Comment_생성_테스트() {
        CommentDto commentDto = new CommentDto(null, CONTENTS, null);
        assertDoesNotThrow(() -> commentService.save(commentDto, EXIST_ARTICLE_ID, userService.findUserById(1L)));
    }

    @Test
    void Comment_조회_테스트() {
        List<CommentDto> commentDtos = commentService.findAllCommentsByArticleId(EXIST_ARTICLE_ID);
        assertThat(commentDtos).hasSize(2);
        assertThat(commentDtos.get(0).getAuthor().getEmail()).isEqualTo(FIRST_EMAIL);
        assertThat(commentDtos.get(1).getAuthor().getEmail()).isEqualTo(SECOND_EMAIL);
    }

    @Test
    void Comment_수정_테스트() {
        CommentDto commentDto = new CommentDto(null, CONTENTS + "123", null);
        assertDoesNotThrow(() -> commentService.modify(1L, commentDto, userService.findUserById(1L)));
    }

    @Test
    void 존재하지않는_Comment_수정_에러_테스트() {
        CommentDto commentDto = new CommentDto(null, CONTENTS + "123", null);
        assertThrows(CommentNotFoundException.class, () -> commentService.modify(3L, commentDto, userService.findUserById(1L)));
    }

    @Test
    void Comment_삭제_테스트() {
        assertDoesNotThrow(() -> commentService.delete(1L, userService.findUserById(1L)));
    }
}
