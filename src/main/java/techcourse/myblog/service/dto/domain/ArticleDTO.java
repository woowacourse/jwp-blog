package techcourse.myblog.service.dto.domain;

import org.springframework.lang.NonNull;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class ArticleDTO {
    public ArticleDTO(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    @NonNull
    private String title;

    @NonNull
    private String contents;

    @NonNull
    private String coverUrl;

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public Article toDomain(User user) {
        return new Article(title, contents, coverUrl, user);
    }
}
