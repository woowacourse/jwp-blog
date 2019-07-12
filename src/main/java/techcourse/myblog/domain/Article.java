package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
	private long id;
	private String title;
	private String contents;
	private String coverUrl;

	public Article(String title, String contents, String coverUrl) {
		this.title = title;
		this.contents = contents;
		this.coverUrl = coverUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public String getContents() {
		return contents;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean matchId(long articleId) {
		return this.id == articleId;
	}

	public void update(Article article) {
		this.title = article.title;
		this.contents = article.contents;
		this.coverUrl = article.coverUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Article)) {
			return false;
		}
		final Article article = (Article) o;
		return getId() == article.getId() &&
				Objects.equals(getTitle(), article.getTitle()) &&
				Objects.equals(getContents(), article.getContents()) &&
				Objects.equals(getCoverUrl(), article.getCoverUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTitle(), getContents(), getCoverUrl());
	}
}
