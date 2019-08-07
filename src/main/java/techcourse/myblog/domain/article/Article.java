package techcourse.myblog.domain.article;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Article {
	private static final int CONTENTS_LENGTH = 1000;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String coverUrl;

	@Column(nullable = false, length = CONTENTS_LENGTH)
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "FK_article_to_user"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User author;

	protected Article() {
	}

	public Article(String title, String coverUrl, String contents, User author) {
		this.title = title;
		this.coverUrl = coverUrl;
		this.contents = contents;
		this.author = author;
	}

	public void updateArticle(Article article) {
		this.title = article.title;
		this.coverUrl = article.coverUrl;
		this.contents = article.contents;
	}

	public boolean matchUserId(Long userId) {
		return this.author.matchId(userId);
	}

	public Long getId() {
		return id;
	}

	public Long getAuthorId() {
		return author.getId();
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
}
