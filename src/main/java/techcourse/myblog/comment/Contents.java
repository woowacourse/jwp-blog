package techcourse.myblog.comment;

import techcourse.myblog.user.User;

public class Contents {
	private String text;

	public Contents(String text) {
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
