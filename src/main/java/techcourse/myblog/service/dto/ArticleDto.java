package techcourse.myblog.service.dto;


import lombok.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.util.List;

public class ArticleDto {

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long id;
        private String contents;
        private String title;
        private String coverUrl;

        public Article toArticle(final User author) {
            return Article.builder()
                    .contents(contents)
                    .coverUrl(coverUrl)
                    .title(title)
                    .author(author)
                    .build();
        }

        public Article toArticle() {
            return Article.builder()
                    .contents(contents)
                    .coverUrl(coverUrl)
                    .title(title)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String contents;
        private String title;
        private String coverUrl;
        private List<Comment> comments;

        Response(Article article) {
            id = article.getId();
            contents = article.getContents();
            title = article.getTitle();
            coverUrl = article.getCoverUrl();
        }

        public static Response createBy(Article article) {
            return new ArticleDto.Response(article);
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }
    }
}
