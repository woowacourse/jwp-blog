package techcourse.myblog.web.dto;

import lombok.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ArticleDto {
    private long id;

    private String title;
    private String contents;
    private String coverUrl;

    public ArticleDto(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public Article toArticle(User author) {
        if(id != 0 ){
            return new Article(id,title,contents,coverUrl,author);
        }
        return new Article(title,contents,coverUrl,author);
    }
}
