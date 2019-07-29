package techcourse.myblog.web.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import javax.persistence.*;

@Data
@Slf4j
public class ArticleDto {
    private long id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article toArticle(User author) {
        if(id != 0 ){
            return new Article(id,title,contents,coverUrl,author);
        }
        return new Article(title,contents,coverUrl,author);
    }
}
