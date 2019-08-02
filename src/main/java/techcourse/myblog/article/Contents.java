package techcourse.myblog.article;

import techcourse.myblog.user.User;

public class Contents {
	private String title;
	private String text;
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

	public Article valueOfArticle() {
		return new Article(this);
	}

	public Article valueOfArticle(User user) {
		return new Article(this, user);
	}

	public Article valueOfArticle(long id, User user) {
		return new Article(id, this, user);
	}
}
