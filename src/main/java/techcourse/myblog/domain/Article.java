package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String coverUrl;
    private String contents;

    public Article() {
    }

    public Article(Long userId, String title, String coverUrl, String contents) {
        this.userId = userId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void updateArticle(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public boolean matchUserId(Long userId) {
        return this.userId.equals(userId);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
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
}
