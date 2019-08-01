package techcourse.myblog.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import techcourse.myblog.domain.vo.article.ArticleVo;

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

	public Article(ArticleVo articleVo) {
		this.title = articleVo.getTitle();
		this.contents = articleVo.getContents();
		this.coverUrl = articleVo.getCoverUrl();
	}

	public Article(ArticleVo articleVo, User user) {
		this.title = articleVo.getTitle();
		this.contents = articleVo.getContents();
		this.coverUrl = articleVo.getCoverUrl();
		this.author = user;
	}

	public Article(Long id, ArticleVo articleVo, User user) {
		this.id = id;
		this.title = articleVo.getTitle();
		this.contents = articleVo.getContents();
		this.coverUrl = articleVo.getCoverUrl();
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
