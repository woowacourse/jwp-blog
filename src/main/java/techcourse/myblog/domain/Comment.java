package techcourse.myblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @ManyToOne
    private User author;

    @ManyToOne
    private Article article;

    public Comment() {
    }

    public Comment(final String contents) {
        this.contents = contents;
    }

    public Comment update(String contents) {
        this.contents = contents;
        return this;
    }

    public boolean isAuthed(User user) {
        return this.author.equals(user);
    }
}
