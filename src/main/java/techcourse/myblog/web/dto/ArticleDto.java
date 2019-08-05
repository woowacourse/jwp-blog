package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Article;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ArticleDto {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String coverUrl;

    @NotNull
    private String contents;

    @NotNull
    private List<CommentDto> comments;

    public ArticleDto(Long id, @NotNull String title, @NotNull String coverUrl, @NotNull String contents, @NotNull List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.comments = comments;
    }

    public static ArticleDto from(Article article) {
        Long id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();
        List<CommentDto> comments = article.getComments().stream()
            .map(CommentDto::from)
            .collect(toList());

        return new ArticleDto(id, title, coverUrl, contents, comments);
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

    public List<CommentDto> getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
