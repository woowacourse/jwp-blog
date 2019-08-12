package techcourse.myblog.article.dto;

import lombok.*;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.dto.UserResponseDto;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ArticleResponseDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;
    private UserResponseDto author;

    @Builder
    public ArticleResponseDto(long id, String title, String coverUrl, String contents, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = UserResponseDto.toUserResponseDto(author);
    }

    public void setAuthor(User author) {
        this.author = UserResponseDto.toUserResponseDto(author);
    }

    public boolean matchAuthorId(long authorId) {
        return author.getId() == authorId;
    }

    public static ArticleResponseDto toArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .coverUrl(article.getCoverUrl())
                .contents(article.getContents())
                .author(article.getAuthor())
                .build();
    }
}