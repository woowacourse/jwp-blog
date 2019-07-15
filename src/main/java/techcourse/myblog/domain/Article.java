package techcourse.myblog.domain;

import java.util.Optional;

public class Article implements Comparable<Article> {
    private int number;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article() {}

    public int getNumber() {
        return this.number;
    }

    public void numbering(ArticleRepository repo) {
        this.number = repo.nextArticleNumber();
    }

    public Article update(Article toUpdate) {
        this.title = toUpdate.title;
        this.coverUrl = toUpdate.coverUrl;
        this.contents = toUpdate.contents;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean validateCoverUrl() {
        return Optional.ofNullable(this.coverUrl).filter(x -> x.length() > 0)
                                                .map(x -> true)
                                                .orElse(false);
    }
    public int compareTo(Article rhs) {
        return this.number - rhs.number;
    }
}