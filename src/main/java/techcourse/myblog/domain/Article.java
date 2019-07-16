package techcourse.myblog.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 5, max = 70, message = "제목을 5~70글자로 입력해주세요.")
    private String title;

    @Column
    @URL
    private String coverUrl;

    @Column
    @Size(min = 5, max = 1000, message = "내용을 5~1000글자로 입력해주세요.")
    private String contents;

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
}
