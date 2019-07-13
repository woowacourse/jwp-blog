package techcourse.myblog.domain;

public class Article {
    private static long articleId = 1;

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    private Article() {
    }

    public Long getId() {
        return id;
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

    public boolean equals(long articleId) {
        return this.id == articleId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String title;
        private String coverUrl;
        private String contents;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder coverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Article build() {
            Article article = new Article();
            article.id = this.id == 0 ? articleId++ : this.id;
            article.title = this.title;
            article.coverUrl = this.coverUrl;
            article.contents = this.contents;

            return article;
        }
    }
}
