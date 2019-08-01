package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.vo.comment.CommentContents;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTests {
	@Test
	void update() {
		CommentContents actualContents = new CommentContents("contentText");
		Comment actualComment = actualContents.valueOf();
		CommentContents updateContents = new CommentContents("updateContentText");
		Comment updateComment = updateContents.valueOf();
		actualComment.update(updateComment);
		assertThat(actualComment.getText()).isEqualTo(updateContents.getText());
	}
}