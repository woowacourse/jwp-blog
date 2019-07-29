package techcourse.myblog.domain.article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDtos {
    private List<ArticleDto> articleDtos;

    public ArticleDtos(Iterable<Article> articles) {
        this.articleDtos = createArticleDtos(articles);
    }

    private List<ArticleDto> createArticleDtos(Iterable<Article> articles) {
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article article : articles) {
            articleDtos.add(ArticleDto.from(article));
        }
        return articleDtos;
    }

    public List<ArticleDto> getArticleDtos() {
        return articleDtos;
    }
}
