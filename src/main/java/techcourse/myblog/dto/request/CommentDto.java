package techcourse.myblog.dto.request;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
	private Long id;
	private String contents;

	public CommentDto(String contents) {
		this.contents = contents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Comment valueOf(User user) {
		return new Comment(user, contents);
	}

	public Comment valueOf() {
		return new Comment(contents);
	}
}
