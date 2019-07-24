package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    public Article() {} // JPA Entity Class에는 기본 생성자가 필요

    public Article(final ArticleDto articleDto) {
        write(articleDto);
    }

    public void write(final ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
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

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (!(another instanceof Article)) return false;
        final Article article = (Article) another;
        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("coverUrl='" + coverUrl + "'")
                .add("contents='" + contents + "'")
                .toString();
    }
}
