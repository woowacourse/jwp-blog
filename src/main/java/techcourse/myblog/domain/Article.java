package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
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
		return Objects.equals(getTitle(), article.getTitle()) &&
				Objects.equals(getContents(), article.getContents()) &&
				Objects.equals(getCoverUrl(), article.getCoverUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTitle(), getContents(), getCoverUrl());
	}
}
