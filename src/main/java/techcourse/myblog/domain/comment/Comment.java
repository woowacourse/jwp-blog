package techcourse.myblog.domain.comment;

import java.time.LocalDateTime;
import javax.persistence.*;

import techcourse.myblog.domain.user.User;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Contents contents;

	@CreatedDate
	@Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;

	@ManyToOne
	@JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
	private User author;

	private Comment() {
	}

	public Comment(User user, Contents contents) {
		this.author = user;
		this.contents = contents;
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return this.contents.getText();
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public User getAuthor() {
		return author;
	}

	public void update(Contents contents) {
		this.contents = contents;
	}

	public boolean matchComment(Comment comment) {
		return this.id.equals(comment.id);
	}
}
