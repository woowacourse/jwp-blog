package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.service.exception.NotFoundCommentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
	private static final Long BASE_USER_ID = 1L;
	private static final Long MISMATCH_USER_ID = 2L;

	@Autowired
	ArticleService articleService;

	@Autowired
	CommentService commentService;

	@Autowired
	UserService userService;

	private Long articleId;
	private UserPublicInfoDto userPublicInfo;
	private UserSessionDto userSession;

	@BeforeEach
	public void setUp() {
		userPublicInfo = userService.findUserPublicInfoById(BASE_USER_ID);
		userSession = new UserSessionDto(BASE_USER_ID, null, null);

		ArticleDto articleDto = articleService.save(
				userSession, new ArticleDto(null, BASE_USER_ID, "title", "coverUrl", "contents"));
		articleId = articleDto.getId();
	}

	@Test
	public void Article_userId와_수정하려는_User의_Id가_다르면_수정_실패() {
		ArticleDto updateArticleDto =
				new ArticleDto(articleId, BASE_USER_ID, "title1", "coverUrl1", "contents1");
		UserSessionDto userSessionDto = new UserSessionDto(MISMATCH_USER_ID, null, null);

		ArticleDto updateFailArticle = articleService.update(articleId, userSessionDto, updateArticleDto);

		assertThat(updateFailArticle.getTitle()).isEqualTo("title");
		assertThat(updateFailArticle.getCoverUrl()).isEqualTo("coverUrl");
		assertThat(updateFailArticle.getContents()).isEqualTo("contents");
	}

	@Test
	public void Article_userId와_삭제하려는_User의_Id가_다르면_삭제_실패() {
		UserSessionDto userSessionDto = new UserSessionDto(MISMATCH_USER_ID, null, null);
		articleService.delete(articleId, userSessionDto);
		ArticleDto deleteFailArticle = articleService.findArticleDtoById(articleId);

		assertThat(deleteFailArticle.getTitle()).isEqualTo("title");
		assertThat(deleteFailArticle.getCoverUrl()).isEqualTo("coverUrl");
		assertThat(deleteFailArticle.getContents()).isEqualTo("contents");
	}

	@Test
	@DisplayName("Article을 삭제했을 때 포함된 Comment도 삭제된다")
	public void deleteArticleWithCascadeComments() {
		CommentRequestDto commentRequestDto = new CommentRequestDto(articleId, "TEST Comment");
		Comment comment = commentService.save(userSession, commentRequestDto);

		UserSessionDto userSessionDto = new UserSessionDto(BASE_USER_ID, null, null);

		articleService.delete(articleId, userSessionDto);
		assertThatThrownBy(() -> commentService.findById(comment.getId()))
				.isInstanceOf(NotFoundCommentException.class);
	}
}
