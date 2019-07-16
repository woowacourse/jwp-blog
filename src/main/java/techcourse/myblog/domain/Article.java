package techcourse.myblog.domain;

import org.thymeleaf.util.StringUtils;

public class Article implements Comparable<Article> {
    private static final String DEFAULT_BACKGROUND_IMAGE_URL = "images/pages/index/study.jpg";

    private int id;
    private String title;
    private String coverUrl = "";
    private String contents;

    public Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateTo(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
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

    public int compareTo(Article article) {
        return this.id - article.id;
    }

    public boolean existsCoverUrl() {
        return !StringUtils.isEmpty(coverUrl);
    }

    public void defaultSetting(int id) {
        if (existsCoverUrl()) {
            coverUrl = DEFAULT_BACKGROUND_IMAGE_URL;
        }
        this.id = id;
    }
}