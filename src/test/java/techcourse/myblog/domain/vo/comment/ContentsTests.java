package techcourse.myblog.domain.vo.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.Contents;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class ContentsTests {
	@Test
	void valueOfComment() {
		Contents contents = new Contents("contentText");
		Comment comment = contents.valueOf();
		assertThat(contents.getText()).isEqualTo(comment.getText());
	}

	@Test
	void valueOfCommentWithUser() {
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@");
		User user = userSignUpInfo.valueOfUser();

		Contents contents = new Contents("contentText");
		Comment comment = contents.valueOf(user);
		assertThat(contents.getText()).isEqualTo(comment.getText());
		assertThat(comment.getAuthor().getEmail()).isEqualTo("tiber@naver.com");
	}
}