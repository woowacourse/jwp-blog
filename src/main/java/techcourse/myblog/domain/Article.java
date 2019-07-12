package techcourse.myblog.domain;

import techcourse.myblog.web.ArticleRequestDto;

import java.util.Objects;

public class Article {
    private static long NEXT_ID = 1;

    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public static Article of(String title, String backgroundURL, String content) {
        Article newArticle = new Article();
        newArticle.setId(NEXT_ID++);
        newArticle.setTitle(title);
        newArticle.setCoverUrl(backgroundURL);
        newArticle.setContents(content);
        return newArticle;
    }

    public static Article from(ArticleRequestDto dto) {
        Article newArticle = new Article();
        newArticle.setId(NEXT_ID++);
        newArticle.setTitle(dto.getTitle());
        newArticle.setCoverUrl(dto.getCoverUrl());
        newArticle.setContents(dto.getContents());
        return newArticle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) &&
            Objects.equals(title, article.title) &&
            Objects.equals(coverUrl, article.coverUrl) &&
            Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
