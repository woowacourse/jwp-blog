package techcourse.myblog.domain.vo.comment;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentContents {
	private String text;

	public CommentContents(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public Comment valueOf() {
		return new Comment(this);
	}

	public Comment valueOf(User user) {
		return new Comment(this, user);
	}
}
