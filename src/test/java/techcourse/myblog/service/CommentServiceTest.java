package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static techcourse.myblog.service.ArticleServiceTest.*;
import static techcourse.myblog.service.UserServiceTest.*;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    private static final Long TEST_COMMENT_ID = 1l;
    private static final Long TEST_ARTICLE_ID = 2l;
    private static final String COMMENTS_CONTENTS = "comment_contents";

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private User user;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, EMAIL, PASSWORD);
        article = new Article(TITLE, COVER_URL, CONTENTS, user);
        comment = new Comment(COMMENTS_CONTENTS, user, article);
    }

    @Test
    @DisplayName("comment를 저장한다.")
    void saveComment() {
        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setContents(COMMENTS_CONTENTS);
        requestCommentDto.setUser(user);
        requestCommentDto.setArticle(article);

        commentService.save(requestCommentDto);

        verify(commentRepository, atLeast(1))
                .save(comment);
    }

    @Test
    @DisplayName("Comment를 조회한다.")
    void findById() {
        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(comment));

        assertThat(commentService.findById(TEST_COMMENT_ID)).isEqualTo(comment);
    }

    @Test
    @DisplayName("comment를 업데이트 한다.")
    void updateComment() {
        // Given
        final String updatedContents = "updated contents";

        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(comment));

        // When
        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setArticleId(TEST_ARTICLE_ID);
        requestCommentDto.setContents(updatedContents);
        Comment updatedComment = commentService.update(requestCommentDto, TEST_COMMENT_ID);

        // Then
        assertThat(updatedComment.getContents()).isEqualTo(updatedContents);
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void deleteComment() {
        article.addComment(comment);
        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(comment));

        commentService.delete(TEST_COMMENT_ID);
        verify(commentRepository, atLeast(1)).deleteById(TEST_COMMENT_ID);
        assertThat(article.getComments()).doesNotContain(comment);
    }
}