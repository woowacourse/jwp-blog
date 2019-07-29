package techcourse.myblog.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public void update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
    }

    public void add(Comment comment) {
        comments.add(comment);
    }

    public void remove(Comment comment) {
        comments.add(comment);
    }
}
