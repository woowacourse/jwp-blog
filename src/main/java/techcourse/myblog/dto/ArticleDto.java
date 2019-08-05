package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.article.ArticleDetails;

public class ArticleDto {
//    @Data
//    public static class Create {
//        @Size(max = 40)
//        @NotBlank
//        private String title;
//
//        @NotBlank
//        private String coverUrl;
//
//        @Size(max = 300)
//        @NotBlank
//        private String contents;
//
//        public Article toArticle(User author) {
//            return Article.builder()
//                    .title(title)
//                    .coverUrl(coverUrl)
//                    .contents(contents)
//                    .author(author)
//                    .build();
//        }
//    }
//
//    @Data
//    public static class Update {
//        @Size(max = 40)
//        @NotBlank
//        private String title;
//
//        @NotBlank
//        private String coverUrl;
//
//        @Size(max = 300)
//        @NotBlank
//        private String contents;
//    }

    @Data
    @EqualsAndHashCode(of = "id")
    public static class Response {
        private Long id;
        private ArticleDetails articleDetails;
    }
}
