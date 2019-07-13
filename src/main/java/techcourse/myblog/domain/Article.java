package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import java.util.concurrent.atomic.AtomicLong;

public class Article {
    private static final AtomicLong nextId = new AtomicLong();

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this.id = nextId.incrementAndGet();
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article(ArticleDto articleDto) {
        this.id = nextId.incrementAndGet();
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public long getId() {
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

    public void updateArticle(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public void updateArticle(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public boolean isSameId(long id) {
        return this.id == id;
    }
}
