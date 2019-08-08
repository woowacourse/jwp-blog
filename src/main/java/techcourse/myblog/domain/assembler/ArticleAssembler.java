package techcourse.myblog.domain.assembler;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.ArticleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleAssembler {
    public static ArticleResponse writeDto(Article article) {
        ArticleResponse articleDto = new ArticleResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents(),
                article.getAuthor(),
                article.getEmail(),
                article.getCountOfComment()
        );

        return articleDto;
    }

    public static List<ArticleResponse> writeDtos(List<Article> articles) {
        return articles.stream()
                .map(ArticleAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static Article writeArticle(ArticleRequest articleDto, User user) {
        return new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), user);
    }
}
