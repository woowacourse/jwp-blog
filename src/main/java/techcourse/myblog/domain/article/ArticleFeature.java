package techcourse.myblog.domain.article;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.article.exception.IllegalContentsException;
import techcourse.myblog.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = {"title", "coverUrl", "contents"})
@Embeddable
public class ArticleFeature {
    private static final int TITLE_LENGTH = 50;
    private final static String TITLE_CONSTRAINT_MESSAGE = "제목은 비어있을 수 없습니다.";
    private final static String CONTENTS_CONSTRAINT_MESSAGE = "내용은 비어있을 수 없습니다.";

    @Column(nullable = false, length = TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    @Lob
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    public ArticleFeature(String title, String coverUrl, String contents) {
        validateEmpty(title, TITLE_CONSTRAINT_MESSAGE);
        validateEmpty(contents, CONTENTS_CONSTRAINT_MESSAGE);
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    private void validateEmpty(String field, String message) {
        if (field == null || field.equals("")) {
            throw new IllegalContentsException(message);
        }
    }

    public Article toArticle(User user) {
        return new Article(this, user);
    }

    @Override
    public String toString() {
        return "ArticleFeature {" +
                "title=" + title +
                ", coverUrl=" + coverUrl +
                ", contents=" + contents +
                '}';
    }
}
