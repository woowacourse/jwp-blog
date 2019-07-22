package techcourse.myblog.domain;

import techcourse.myblog.application.dto.ArticleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    private Article() {

    }

    public static class ArticleBuilder {
        private String title;
        private String coverUrl;
        private String contents;

        public ArticleBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleBuilder coverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
            return this;
        }

        public ArticleBuilder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

    public Article(ArticleBuilder articleBuilder) {
        this.title = articleBuilder.title;
        this.coverUrl = articleBuilder.coverUrl;
        this.contents = articleBuilder.contents;
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void modify(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Article article = (Article) object;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}