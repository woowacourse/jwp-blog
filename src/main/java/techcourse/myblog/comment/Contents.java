package techcourse.myblog.comment;

import javax.persistence.Embeddable;

@Embeddable
public class Contents {
	private String text;

	private Contents() {
	}

	public Contents(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
