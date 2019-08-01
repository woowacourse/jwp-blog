package techcourse.myblog.domain;

public class ArticleVo {
	private String title;
	private String contents;
	private String coverUrl;

	private ArticleVo() {}

	public ArticleVo(String title, String contents, String coverUrl) {
		this.title = title;
		this.contents = contents;
		this.coverUrl = coverUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
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
