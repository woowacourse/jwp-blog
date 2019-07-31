package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.NotFoundCommentException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {
	private static final Long BASE_USER_ID = 1L;
	private static final Long MISMATCH_USER_ID = 2L;
	public static final String TEST_COMMENT = "TEST Comment";
	public static final String UPDATE_COMMENT = "UPDATE Comment";

	@Autowired
	private CommentService commentService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	private Long articleId;
	private UserPublicInfoDto userPublicInfo;

	@BeforeEach
	public void setUp() {
		ArticleDto articleDto = articleService.save(BASE_USER_ID,
				new ArticleDto(null, BASE_USER_ID, "title", "url", "contents"));
		articleId = articleDto.getId();

		userPublicInfo = userService.findUserPublicInfoById(BASE_USER_ID);
	}

	@Test
	public void saveComment() {
		CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, TEST_COMMENT);
		Comment comment = commentService.save(userPublicInfo, commentRequestDto);

		assertThat(comment.getComment()).isEqualTo(commentRequestDto.getComment());
	}

	@Test
	public void findCommentsByArticleId() {
		CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, TEST_COMMENT);
		Comment comment1 = commentService.save(userPublicInfo, commentRequestDto);
		Comment comment2 = commentService.save(userPublicInfo, commentRequestDto);
		List<CommentResponseDto> comments = commentService.findCommentsByArticleId(articleId);

		assertThat(comments.size()).isEqualTo(2);
		assertThat(comments.get(0).getComment()).isEqualTo(comment1.getComment());
		assertThat(comments.get(0).getAuthorName()).isEqualTo(comment1.getAuthorName());
		assertThat(comments.get(1).getComment()).isEqualTo(comment2.getComment());
		assertThat(comments.get(1).getAuthorName()).isEqualTo(comment2.getAuthorName());
	}

	@Test
	@DisplayName("정상 수정 테스트")
	public void updateComment() {
		Comment comment = commentService.findById(BASE_USER_ID);
		CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, UPDATE_COMMENT);
		UserPublicInfoDto userPublicInfo = userService.findUserPublicInfoById(BASE_USER_ID);

		commentService.update(userPublicInfo, comment.getId(), updateRequestDto);
		Comment updatedComment = commentService.findById(BASE_USER_ID);
		assertThat(updatedComment.getComment()).isEqualTo(UPDATE_COMMENT);
	}

	@Test
	@DisplayName("Comment를 등록한 User가 다를때 수정 실패")
	public void failUpdatingCommentWhenMismatchUser() {
		Comment comment = commentService.findById(BASE_USER_ID);
		CommentRequestDto updateRequestDto = new CommentRequestDto(articleId, UPDATE_COMMENT);
		UserPublicInfoDto userPublicInfo = userService.findUserPublicInfoById(MISMATCH_USER_ID);

		commentService.update(userPublicInfo, comment.getId(), updateRequestDto);
		Comment updatedComment = commentService.findById(BASE_USER_ID);
		assertThat(updatedComment.getComment()).isEqualTo(comment.getComment());
	}

	@Test
	@DisplayName("정상 삭제 테스트")
	public void deleteComment() {
		CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, TEST_COMMENT);
		Comment deletedComment = commentService.save(userPublicInfo, commentRequestDto);
		UserPublicInfoDto userPublicInfo = userService.findUserPublicInfoById(BASE_USER_ID);

		commentService.delete(userPublicInfo, deletedComment.getId());
		assertThatThrownBy(() -> commentService.findById(deletedComment.getId()))
				.isInstanceOf(NotFoundCommentException.class);
	}

	@Test
	@DisplayName("Comment를 등록한 User가 다를때 삭제 실패")
	public void failDeletingCommentWhenMismatchUser() {
		CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, TEST_COMMENT);
		Comment deletedComment = commentService.save(userPublicInfo, commentRequestDto);
		UserPublicInfoDto userPublicInfo = userService.findUserPublicInfoById(MISMATCH_USER_ID);

		commentService.delete(userPublicInfo, deletedComment.getId());
		assertThat(commentService.findCommentsByArticleId(articleId).size()).isEqualTo(1);
	}
}