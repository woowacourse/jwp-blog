package techcourse.myblog.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import techcourse.myblog.dto.request.ArticleDto;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String contents;

	private String title;
	private String coverUrl;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
	private User author;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "article_id", foreignKey = @ForeignKey(name  = "fk_article_to_comment"))
	private List<Comment> comments;

	private Article() {
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

	public User getAuthor() {
		return author;
	}

	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}

	public void update(Article article) {
		this.title = article.title;
		this.contents = article.contents;
		this.coverUrl = article.coverUrl;
	}

	public boolean matchArticle(Article expectedArticle) {
		return this.id.equals(expectedArticle.id);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
