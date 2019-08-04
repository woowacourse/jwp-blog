package techcourse.myblog.dto;

import lombok.Data;

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
    public static class Response {
        private Long id;
        private String title;
        private String coverUrl;
        private String contents;
    }
}
