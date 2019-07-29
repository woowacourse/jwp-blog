package techcourse.myblog.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String contents;

	@CreatedDate
	@Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
	private User author;

	@ManyToOne
	@JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
	private Article article;

	public Comment() {
	}

	public Comment(String contents) {
		this.contents = contents;
	}

	public Comment(User user, Article article, String contents) {
		this.author = user;
		this.article = article;
		this.contents = contents;
	}

	public Long getId() {
		return id;
	}

	public String getContents() {
		return contents;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public User getAuthor() {
		return author;
	}

	public Article getArticle() {
		return article;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Comment)) {
			return false;
		}
		final Comment comment = (Comment) o;
		return Objects.equals(getId(), comment.getId()) &&
				Objects.equals(contents, comment.contents) &&
				Objects.equals(createDate, comment.createDate) &&
				Objects.equals(author, comment.author) &&
				Objects.equals(article, comment.article);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), contents, createDate, author, article);
	}

	public void update(Comment comment) {
		this.contents = comment.contents;
	}
}
