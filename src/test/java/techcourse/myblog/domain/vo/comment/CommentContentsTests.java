package techcourse.myblog.domain.vo.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.vo.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class CommentContentsTests {
	@Test
	void valueOfComment() {
		CommentContents commentContents = new CommentContents("contentText");
		Comment comment = commentContents.valueOf();
		assertThat(commentContents.getText()).isEqualTo(comment.getText());
	}

	@Test
	void valueOfCommentWithUser() {
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@");
		User user = userSignUpInfo.valueOfUser();

		CommentContents commentContents = new CommentContents("contentText");
		Comment comment = commentContents.valueOf(user);
		assertThat(commentContents.getText()).isEqualTo(comment.getText());
		assertThat(comment.getAuthor().getEmail()).isEqualTo("tiber@naver.com");
	}
}