package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import java.util.Objects;

public class Article {
    private static final String DEFAULT_BACKGROUND = "/images/default/bg.jpg";
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = getDefaultCoverUrl(coverUrl);
    }

    public Article(ArticleDto articleDto) {
        this(articleDto.getTitle(), articleDto.getContents(), articleDto.getCoverUrl());
    }

    private String getDefaultCoverUrl(String coverUrl) {
        if (coverUrl.isEmpty()) {
            return DEFAULT_BACKGROUND;
        }
        return coverUrl;
    }

    public void updateByArticle(ArticleDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.coverUrl = dto.getCoverUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title) &&
                Objects.equals(contents, article.contents) &&
                Objects.equals(coverUrl, article.coverUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, contents, coverUrl);
    }
}
