package techcourse.myblog.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.dto.ArticleResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.dto.UserResponseDto;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CommentResponseDto {
    private long id;
    private String contents;
    private UserResponseDto author;
    private ArticleResponseDto article;

    public void setAuthor(User author) {
        this.author = UserResponseDto.toUserResponseDto(author);
    }

    public void setArticle(Article article) {
        this.article = ArticleResponseDto.toArticleResponseDto(article);
    }
}
