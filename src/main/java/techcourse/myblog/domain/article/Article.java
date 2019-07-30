package techcourse.myblog.domain.article;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ArticleVo articleVo;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    private Article() {
    }

    public Article(User author, ArticleVo articleVo) {
        this.author = author;
        this.articleVo = articleVo;
    }

    public void updateArticle(ArticleVo articleVo) {
        this.articleVo = articleVo;
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
        return articleVo.getTitle();
    }

    public String getCoverUrl() {
        return articleVo.getCoverUrl();
    }

    public String getContents() {
        return articleVo.getContents();
    }
}
