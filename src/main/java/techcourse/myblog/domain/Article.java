package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleRequestDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    private Article() {

    }

    public static Article of(ArticleRequestDto articleRequestDto) {
        Article newArticle = new Article();

        newArticle.title = articleRequestDto.getTitle();
        newArticle.coverUrl = articleRequestDto.getCoverUrl();
        newArticle.contents = articleRequestDto.getContents();

        return newArticle;
    }

    public Long getId() {
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

    public void update(ArticleRequestDto articleRequestDto) {
        this.title = articleRequestDto.getTitle();
        this.coverUrl = articleRequestDto.getCoverUrl();
        this.contents = articleRequestDto.getContents();
    }
}
