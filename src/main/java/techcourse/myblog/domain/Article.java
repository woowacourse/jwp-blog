package techcourse.myblog.domain;

import techcourse.myblog.domain.exception.IllegalArticleArgumentsException;
import techcourse.myblog.web.ArticleDto;

public class Article {
    private final int articleId;
    private String title;
    private String coverUrl;
    private String contents;

    Article(int articleId, String title, String coverUrl, String contents) {
        this.articleId = articleId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public static Article of(int articleId, ArticleDto articleDTO) {
        validateNotNull(articleDTO);

        return new Article(
                articleId,
                articleDTO.getTitle(),
                articleDTO.getCoverUrl(),
                articleDTO.getContents()
        );
    }

    private static void validateNotNull(ArticleDto articleDTO) {
        if (articleDTO.getTitle() == null
                || articleDTO.getCoverUrl() == null
                || articleDTO.getContents() == null) {
            throw new IllegalArticleArgumentsException();
        }
    }

    public int getArticleId() {
        return articleId;
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
}
