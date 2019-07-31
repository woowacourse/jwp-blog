package techcourse.myblog.domain;

import java.time.LocalDateTime;
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

	public void update(Comment comment) {
		this.contents = comment.contents;
	}

	public boolean matchComment(Comment expectComment) {
		return this.id.equals(expectComment.id);
	}
}
