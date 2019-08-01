package techcourse.myblog.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import techcourse.myblog.domain.vo.comment.CommentContents;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String text;

	@CreatedDate
	@Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
	private User author;

	private Comment() {
	}

	public Comment(CommentContents commentContents) {
		this.text = commentContents.getText();
	}

	public Comment(CommentContents commentContents, User user) {
		this.author = user;
		this.text = commentContents.getText();
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public User getAuthor() {
		return author;
	}

	public void update(Comment comment) {
		this.text = comment.text;
	}

	public boolean matchComment(Comment comment) {
		return this.id.equals(comment.id);
	}
}
