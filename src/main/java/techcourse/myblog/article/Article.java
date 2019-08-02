package techcourse.myblog.article;

import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import techcourse.myblog.comment.Comment;
import techcourse.myblog.user.User;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String text;

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

	public Article(Contents contents) {
		this.title = contents.getTitle();
		this.text = contents.getText();
		this.coverUrl = contents.getCoverUrl();
	}

	public Article(Contents contents, User user) {
		this.title = contents.getTitle();
		this.text = contents.getText();
		this.coverUrl = contents.getCoverUrl();
		this.author = user;
	}

	public Article(Long id, Contents contents, User user) {
		this.id = id;
		this.title = contents.getTitle();
		this.text = contents.getText();
		this.coverUrl = contents.getCoverUrl();
		this.author = user;
	}

	public String getTitle() {
		return title;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public String getText() {
		return text;
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
	//todo: VO가 아닌 Article 클래스로 받는게 맞을까?
	public void update(Article article) {
		this.title = article.title;
		this.text = article.text;
		this.coverUrl = article.coverUrl;
	}

	public boolean matchArticle(Article expectedArticle) {
		return this.id.equals(expectedArticle.id);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
