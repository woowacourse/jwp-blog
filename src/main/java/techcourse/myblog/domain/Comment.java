package techcourse.myblog.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @ManyToOne
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Article article;

    public Comment(final String contents) {
        this.contents = contents;
    }

    public void update(Comment comment) {
        contents = comment.getContents();
    }
}
