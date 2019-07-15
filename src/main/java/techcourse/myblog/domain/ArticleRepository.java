package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.dto.ArticleDTO;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final String NO_SUCH_ARTICLE = "그런 게시물을 찾지 못했습니다.";

    private final List<Article> articles = new ArrayList<>();
    private int index = 1;

    public List<Article> getAll() {
        return articles;
    }

    public int addArticle(final ArticleDTO articleDTO) {
        final Article article = new Article(
                this.index,
                articleDTO.getTitle(),
                articleDTO.getCoverUrl(),
                articleDTO.getContents()
        );
        articles.add(article);
        this.index++;
        return article.getId();
    }

    public Article findById(final int articleId) {
        return articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(NO_SUCH_ARTICLE));
    }

    public void updateById(final int articleId, final ArticleDTO newArticleDTO) {
        final int index = articles.indexOf(findById(articleId));
        final Article newArticle = new Article(
                articleId,
                newArticleDTO.getTitle(),
                newArticleDTO.getCoverUrl(),
                newArticleDTO.getContents()
        );
        articles.set(index, newArticle);
    }

    public void deleteById(final int articleId) {
        final Article article = findById(articleId);
        articles.remove(article);
    }
}
