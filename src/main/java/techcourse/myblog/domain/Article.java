package techcourse.myblog.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String contents;
	private String coverUrl;

	public Article() {
	}

	public Article(String title, String contents, String coverUrl) {
		this.title = title;
		this.contents = contents;
		this.coverUrl = coverUrl;
	}

	public Article(Long id, String title, String contents, String coverUrl) {
		this.id = id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean matchId(Long articleId) {
		return this.id.equals(articleId);
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
		return Objects.equals(getId(), article.getId()) &&
				Objects.equals(getTitle(), article.getTitle()) &&
				Objects.equals(getContents(), article.getContents()) &&
				Objects.equals(getCoverUrl(), article.getCoverUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTitle(), getContents(), getCoverUrl());
	}
}
