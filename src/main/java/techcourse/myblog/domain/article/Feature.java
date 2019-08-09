package techcourse.myblog.domain.article;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;
import techcourse.myblog.domain.article.exception.IllegalContentsException;
import techcourse.myblog.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = {"title", "coverUrl", "contents"})
@Embeddable
public class Feature {
    private static final int TITLE_LENGTH = 50;

    @Column(nullable = false, length = TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    @Lob
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    public Feature(String title, String coverUrl, String contents) {
        validateEmpty(title, "제목은 비어있을 수 없습니다.");
        validateEmpty(contents, "내용은 비어있을 수 없습니다.");
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    private void validateEmpty(String title, String s) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalContentsException(s);
        }
    }

    public Article toArticle(User user) {
        return new Article(this, user);
    }

    @Override
    public String toString() {
        return "Feature {" +
                "title=" + title +
                ", coverUrl=" + coverUrl +
                ", contents=" + contents +
                '}';
    }
}
