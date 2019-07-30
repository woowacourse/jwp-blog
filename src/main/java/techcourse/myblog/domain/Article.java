package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class Article {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    public Article() {} // JPA Entity Class에는 기본 생성자가 필요

    public Article(final String title, final String coverUrl, final String contents, final User author) {
        this.title = title.trim();
        this.coverUrl = coverUrl.trim();
        this.contents = contents.trim();
        this.author = author;
    }

    public Article(final ArticleDto articleDto) {
        update(articleDto);
    }

    public Article update(final ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
        return this;
    }

    public Long getId() {
        return id;
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

    public User getAuthor() {
        return author;
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (!(another instanceof Article)) return false;
        final Article article = (Article) another;
        return id.equals(article.id) &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents) &&
                author.equals(article.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents, author);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("coverUrl='" + coverUrl + "'")
                .add("contents='" + contents + "'")
                .add("author=" + author)
                .toString();
    }
}
