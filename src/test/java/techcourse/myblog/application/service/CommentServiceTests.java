package techcourse.myblog.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.assembler.ArticleAssembler;
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
    private static final CommentDto requestCommentDto = new CommentDto(null, CONTENTS, null);

    private CommentService commentService;

    private ArticleAssembler articleAssembler = ArticleAssembler.getInstance();

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleService articleService;
    @Mock
    private UserService userService;

    @Mock
    private User firstUser;
    @Mock
    private User secondUser;
    @Mock
    private Article article;

    private Comment firstComment;
    private Comment firstSavedComment;
    private Comment secondComment;

    public CommentServiceTests() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository, userService, articleService);
        initUserMock();
        initArticleMock();
        initCommentMock();
        initUserServiceMock();
        initArticleServiceMock();
        initCommentRepository();
    }

    private void initUserMock() {
        when(firstUser.getId()).thenReturn(1L);
        when(firstUser.getName()).thenReturn(NAME);
        when(firstUser.getEmail()).thenReturn(FIRST_EMAIL);
        when(firstUser.getPassword()).thenReturn(PASSWORD);
        when(secondUser.getId()).thenReturn(2L);
        when(secondUser.getName()).thenReturn(NAME);
        when(secondUser.getEmail()).thenReturn(SECOND_EMAIL);
        when(secondUser.getPassword()).thenReturn(PASSWORD);
    }

    private void initArticleMock() {
        when(article.getAuthor()).thenReturn(firstUser);
        when(article.getId()).thenReturn(EXIST_ARTICLE_ID);
        when(article.getContents()).thenReturn(CONTENTS);
        when(article.getTitle()).thenReturn(TITLE);
        when(article.getCoverUrl()).thenReturn(COVER_URL);
    }

    private void initCommentMock() {
        firstComment = new Comment.CommentBuilder()
                .article(article)
                .author(firstUser)
                .contents(requestCommentDto.getContents())
                .build();
        firstSavedComment = new Comment(1L, CONTENTS, firstUser, article);
        secondComment = new Comment(2L, CONTENTS, secondUser, article);
    }

    private void initUserServiceMock() {
        when(userService.findUserById(1L)).thenReturn(firstUser);
        when(userService.findUserById(2L)).thenReturn(secondUser);
    }

    private void initArticleServiceMock() {
        ArticleDto articleDto = articleAssembler.convertEntityToDto(article);
        when(articleService.findById(EXIST_ARTICLE_ID)).thenReturn(articleDto);
        when(articleService.findArticleById(EXIST_ARTICLE_ID)).thenReturn(article);
    }

    private void initCommentRepository() {
        Article article = articleService.findArticleById(EXIST_ARTICLE_ID);
        when(commentRepository.save(firstComment)).thenReturn(firstSavedComment);
        when(commentRepository.findByArticle(article)).thenReturn(Arrays.asList(firstComment, secondComment));
        Mockito.doNothing().when(commentRepository).deleteById(1L);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(firstComment));
        when(commentRepository.findById(2L)).thenReturn(Optional.of(secondComment));
        when(commentRepository.findById(3L)).thenReturn(Optional.empty());
    }

    @Test
    void Comment_생성_테스트() {
        assertDoesNotThrow(() -> commentService.save(requestCommentDto, EXIST_ARTICLE_ID, userService.findUserById(1L)));
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
