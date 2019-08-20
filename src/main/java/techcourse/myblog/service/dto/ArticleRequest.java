package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class ArticleRequest {
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleRequest(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article toArticle() {
        return new Article(title, coverUrl, contents);
    }

    public Article addAuthorAndToArticle(User author) {
        return new Article(title, coverUrl, contents, author);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ArticleRequest{" +
                "title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
