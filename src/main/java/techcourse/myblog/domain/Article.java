package techcourse.myblog.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import techcourse.myblog.domain.vo.article.ArticleContents;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String contents;

	private String text;
	private String coverUrl;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
	private User author;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "article_id", foreignKey = @ForeignKey(name  = "fk_article_to_comment"))
	private List<Comment> comments;

	private Article() {
	}

	public Article(ArticleContents articleContents) {
		this.text = articleContents.getTitle();
		this.contents = articleContents.getText();
		this.coverUrl = articleContents.getCoverUrl();
	}

	public Article(ArticleContents articleContents, User user) {
		this.text = articleContents.getTitle();
		this.contents = articleContents.getText();
		this.coverUrl = articleContents.getCoverUrl();
		this.author = user;
	}

	public Article(Long id, ArticleContents articleContents, User user) {
		this.id = id;
		this.text = articleContents.getTitle();
		this.contents = articleContents.getText();
		this.coverUrl = articleContents.getCoverUrl();
		this.author = user;
	}

	public String getText() {
		return text;
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
		this.text = article.text;
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
