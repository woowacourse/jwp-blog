package techcourse.myblog.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ARTICLE_ID")
    private Long id;

    @NonNull
    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @NonNull
    @Column(name = "COVER_URL")
    private String coverUrl;

    @NonNull
    @Column(name = "CONTENTS", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String contents;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User user;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    public void addComments(Comment comment) {
        this.comments.add(comment);
        if(comment.getArticle() != this) {
            comment.setArticle(this);
        }
    }

    public List<Comment> getComments() {
        System.out.println(comments.isEmpty());
        return comments;
    }

    public Article update(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
        this.user = article.user;

        return this;
    }


}
