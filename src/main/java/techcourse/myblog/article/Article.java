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

	@Embedded
	private Contents contents;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
	private User author;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_to_comment"))
	private List<Comment> comments;

	private Article() {
	}

	public Article(Contents contents) {
		this.contents = contents;
	}

	public Article(User user, Contents contents) {
		this.author = user;
		this.contents = contents;
	}

	public String getTitle() {
		return this.contents.getTitle();
	}

	public String getCoverUrl() {
		return this.contents.getCoverUrl();
	}

	public String getText() {
		return this.contents.getText();
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

	public void update(Contents contents) {
		this.contents = contents;
	}

	public Contents getContents() {
		return contents;
	}

	public boolean matchArticle(Article expectedArticle) {
		return this.id.equals(expectedArticle.id);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
