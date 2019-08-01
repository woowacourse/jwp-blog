package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.application.dto.CommentRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    private Comment() {
    }

    public Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getAuthor() {
        return author;
    }

    public Article getArticle() {
        return article;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public boolean isSameAuthor(Long authorId) {
        return this.author.getId().equals(authorId);
    }

    // TODO : Entity와 DTO의 연관관계가 생김     ㅜ여기서
    // CommentRequest에 userId가 있으면 user를 받아줄 필요없이 처리 가능할 수도?
    // comment에 user가 있으면 직접참조, user_id가 있으면 간접참조
    public void changeContents(CommentRequest commentRequest, Long authorId) {
        if (!isSameAuthor(authorId)) {
            throw new IllegalArgumentException();
        }
        this.contents = commentRequest.getContents();
    }
}
