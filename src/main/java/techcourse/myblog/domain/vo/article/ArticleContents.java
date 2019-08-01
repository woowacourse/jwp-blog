package techcourse.myblog.domain.vo.article;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class ArticleContents {
	private String title;
	private String text;
	private String coverUrl;

	private ArticleContents() {}

	public ArticleContents(String title, String text, String coverUrl) {
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
