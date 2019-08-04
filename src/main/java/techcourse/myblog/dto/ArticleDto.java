package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class ArticleDto {
    @Data
    public static class Create {
        private String title;
        private String coverUrl;
        private String contents;

        //최초 save는 Id값 불필요
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
    public static class Update {
        private String title;
        private String coverUrl;
        private String contents;

        //save 하기 위해 Id값 필요
        public Article toArticle(Long articleId, User author) {
            return Article.builder()
                    .id(articleId)
                    .title(title)
                    .coverUrl(coverUrl)
                    .contents(contents)
                    .author(author)
                    .build();
        }
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String coverUrl;
        private String contents;
    }
}
