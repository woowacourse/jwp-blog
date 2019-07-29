package techcourse.myblog.domain;

import java.util.Objects;
import javax.persistence.*;

import techcourse.myblog.dto.request.ArticleDto;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String contents;
	private String coverUrl;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
	private User author;

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

	public Article(ArticleDto articleDto, User user) {
		this.title = articleDto.getTitle();
		this.contents = articleDto.getContents();
		this.coverUrl = articleDto.getCoverUrl();
		this.author = user;
	}

	public Article(Long id, ArticleDto articleDto, User user) {
		this.id = id;
		this.title = articleDto.getTitle();
		this.contents = articleDto.getContents();
		this.coverUrl = articleDto.getCoverUrl();
		this.author = user;
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

	public void update(Article article) {
		this.title = article.title;
		this.contents = article.contents;
		this.coverUrl = article.coverUrl;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public boolean matchUser(User user) {
		return this.author.equals(user);
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
				Objects.equals(getCoverUrl(), article.getCoverUrl()) &&
				Objects.equals(getAuthor(), article.getAuthor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTitle(), getContents(), getCoverUrl(), getAuthor());
	}
}
