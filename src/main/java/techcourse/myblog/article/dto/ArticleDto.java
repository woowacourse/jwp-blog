package techcourse.myblog.article.dto;

import lombok.Data;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;

public class ArticleDto {

    @Data
    public static class Creation {
        private String title;
        private String coverUrl;
        private String contents;

        public Article toArticle(User author) {
            return Article.builder()
                    .title(title)
                    .coverUrl(coverUrl)
                    .contents(contents)
                    .author(author)
                    .build();
        }
    }

    @Data
    public static class Updation {
        private String title;
        private String coverUrl;
        private String contents;

        public Article toArticle(long articleId) {
            return Article.builder()
                    .id(articleId)
                    .title(title)
                    .coverUrl(coverUrl)
                    .contents(contents)
                    .build();
        }
    }

    @Data
    public static class Response {
        private long id;
        private String title;
        private String coverUrl;
        private String contents;
        private User author;
    }
}