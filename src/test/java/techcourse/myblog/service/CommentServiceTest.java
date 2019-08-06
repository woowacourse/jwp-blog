package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.ResponseCommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    private static final String COMMENTS_CONTENTS = "comment_contents";
    private static final String COMMENTS_CONTENTS_2 = "comment_contents2";
    private static final Long TEST_COMMENT_ID = 1l;
    private static final Long TEST_ARTICLE_ID = 2l;
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password!1";
    private static final String TITLE = "title";
    private static final String COVER_URL = "cover_url";
    private static final String CONTENTS = "contents";
    private static final User USER = new User(USER_NAME, EMAIL, PASSWORD);
    private static final Article ARTICLE = new Article(TITLE, COVER_URL, CONTENTS, USER);

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("comment 잘 저장한다.")
    void save() {
        RequestCommentDto requestCommentDto = new RequestCommentDto(TEST_ARTICLE_ID, COMMENTS_CONTENTS);
        given(articleRepository.findById(TEST_ARTICLE_ID)).willReturn(Optional.of(ARTICLE));
        commentService.save(requestCommentDto, USER);

        verify(commentRepository, atLeast(1)).save(new Comment(COMMENTS_CONTENTS, USER, ARTICLE));
    }

    @Test
    @DisplayName("Comment를 잘 조회한다.")
    void findById() {
        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(new Comment(COMMENTS_CONTENTS, USER, ARTICLE)));
        Comment foundComment = commentService.findById(TEST_COMMENT_ID, USER);

        assertThat(foundComment).isEqualTo(new Comment(COMMENTS_CONTENTS, USER, ARTICLE));
    }

    @Test
    @DisplayName("comment를 업데이트 한다.")
    void update() {
        RequestCommentDto requestCommentDto = new RequestCommentDto(TEST_ARTICLE_ID, COMMENTS_CONTENTS_2);

        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(new Comment(COMMENTS_CONTENTS, USER, ARTICLE)));

        ResponseCommentDto updatedComment = commentService.update(requestCommentDto, TEST_COMMENT_ID, USER);

        assertThat(updatedComment.getContents()).isEqualTo(COMMENTS_CONTENTS_2);
    }

    @Test
    @DisplayName("comment를 삭제한다.")
    void delete() {
        Comment comment = new Comment(COMMENTS_CONTENTS, USER, ARTICLE);
        ARTICLE.addComment(comment);
        given(commentRepository.findById(TEST_COMMENT_ID))
                .willReturn(Optional.of(comment));

        commentService.delete(TEST_COMMENT_ID, USER);
        verify(commentRepository, atLeast(1)).deleteById(TEST_COMMENT_ID);
        assertThat(ARTICLE.getComments()).doesNotContain(comment);
    }
}