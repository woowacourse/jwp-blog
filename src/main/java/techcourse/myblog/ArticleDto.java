package techcourse.myblog;

import techcourse.myblog.domain.Article;

public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(final String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    public Article convertArticle(final int id) {
        return new Article(id, title, coverUrl, contents);
    }
}
