package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.Contents;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTests {
	@Test
	void update() {
		Contents actualContents = new Contents("contentText");
		Comment actualComment = actualContents.valueOf();
		Contents updateContents = new Contents("updateContentText");
		Comment updateComment = updateContents.valueOf();
		actualComment.update(updateComment);
		assertThat(actualComment.getText()).isEqualTo(updateContents.getText());
	}
}