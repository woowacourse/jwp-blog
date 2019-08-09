package techcourse.myblog.application.dto;

import lombok.Getter;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Deprecated
public class ArticleDto {
    private final static String TITLE_CONSTRAINT_MESSAGE = "제목을 입력해주세요.";
    private final static String CONTENTS_CONSTRAINT_MESSAGE = "내용을 입력해주세요.";

    @NotBlank(message = TITLE_CONSTRAINT_MESSAGE)
    private String title;

    @NotNull
    private String coverUrl;

    @NotBlank(message = CONTENTS_CONSTRAINT_MESSAGE)
    private String contents;
    
    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

//    public Article toArticle(User author) {
//        return new Article(title, coverUrl, contents, author);
//    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
