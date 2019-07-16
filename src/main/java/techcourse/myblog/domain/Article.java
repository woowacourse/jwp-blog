package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
    private static final String EMPTY_TEXT = "NULL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    public Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        validateTitle(title);
        validateContents(contents);
        this.title = title;
        this.contents = contents;
        this.coverUrl = (isBlank(coverUrl)) ? EMPTY_TEXT : coverUrl;
    }

    private void validateTitle(String title) {
        if (isBlank(title)) {
            throw new InvalidArticleException("제목을 입력해주세요!");
        }
    }

    private void validateContents(String contents) {
        if (isBlank(contents)) {
            throw new InvalidArticleException("내용을 입력해주세요!");
        }
    }

    private boolean isBlank(String text) {
        return text == null || "".equals(text);
    }

    public void update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
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

    public Long getId() {
        return id;
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
