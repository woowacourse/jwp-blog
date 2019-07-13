package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private final int id;
    private String title;
    private String coverUrl;
    private String contents;
    
    private Article(int id, ArticleDto articleDto) {
        this.id = id;
        edit(articleDto);
    }
    
    public static Article to(int id, ArticleDto articleDto) {
        return new Article(id, articleDto);
    }
    
    public boolean matchId(int id) {
        return this.id == id;
    }
    
    public void edit(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}