package techcourse.myblog.domain;

import techcourse.myblog.domain.dto.ArticleDto;

import java.util.Objects;

public class Article {
    private int id;
    private String title;
    private String coverUrl;
    private String contents;

//    public Article(String title, String coverUrl, String contents) {
//        this.title = title;
//        this.coverUrl = coverUrl;
//        this.contents = contents;
//    }

    public Article(int id, ArticleDto articleDto) {
        this.id = id;
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public boolean matchId(int id) {
        return this.id == id;
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

//    public Article insertId(int id, Article article) {
//        return new Article(id, article.getTitle(), article.getCoverUrl(), article.getContents());
//    }

    public void update(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
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