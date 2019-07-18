package techcourse.myblog.domain;

import techcourse.myblog.exception.ArticleToUpdateNotFoundException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;


@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article() {
    }

    public Article(final String title, final String coverUrl, final String contents) {
        checkNull(title, coverUrl, contents);
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    private void checkNull(String title, String coverUrl, String contents) {
        if (Objects.isNull(title) || Objects.isNull(coverUrl) || Objects.isNull(contents)) {
            throw new NullPointerException();
        }
    }

    public void update(final Article article) {
        if (Objects.isNull(article)) {
            throw new ArticleToUpdateNotFoundException("업데이트 해야할 게시글이 없습니다.");
        }
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
    }

    public int getId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
