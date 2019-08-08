package techcourse.myblog.domain.article;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
@Getter
public class Contents {
    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String coverUrl;

    @Lob
    private String contents;

    private Contents() {
    }

    public Contents(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contents contents1 = (Contents) o;
        return Objects.equals(title, contents1.title) &&
                Objects.equals(coverUrl, contents1.coverUrl) &&
                Objects.equals(contents, contents1.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, coverUrl, contents);
    }
}
