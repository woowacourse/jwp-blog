package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.ArticleResponse;

public class ArticleAssembler {
    public static Article toEntity(ArticleRequest dto, User author) {
        String title = dto.getTitle();
        String contents = dto.getContents();
        String coverUrl = dto.getCoverUrl();

        return new Article(title, contents, coverUrl, author);
    }

    public static ArticleResponse toDto(Article article) {
        Long id = article.getId();
        String title = article.getTitle();
        String contents = article.getContents();
        String coverUrl = article.getCoverUrl();
        User author = getAuthorWithoutPassword(article.getAuthor());

        return new ArticleResponse(id, title, contents, coverUrl, author);
    }

    private static User getAuthorWithoutPassword(User author) {
        return new User(author.getName(), author.getEmail());
    }
}
