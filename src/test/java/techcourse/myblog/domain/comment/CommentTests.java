package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.Contents;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTests {
	@Test
	void update() {
		Contents actualContents = new Contents("contentText");
		Comment actualComment = new Comment(actualContents);
		Contents updateContents = new Contents("updateContentText");
		Comment updateComment = new Comment(updateContents);
		actualComment.update(updateContents);
		assertThat(actualComment.getText()).isEqualTo(updateComment.getText());
	}
}