package techcourse.myblog.article;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.springframework.lang.Nullable;

@Embeddable
public class Contents {
	@Lob
	private String text;

	private String title;

	@Nullable
	private String coverUrl;

	private Contents() {}

	public Contents(String title, String text, String coverUrl) {
		this.title = title;
		this.text = text;
		this.coverUrl = coverUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getCoverUrl() {
		return coverUrl;
	}
}
