package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String contents;

    @ManyToOne
    @NonNull
    private Article article;

    @ManyToOne
    @NonNull
    private User author;

    public void update(Comment comment) {
        this.contents = comment.contents;
    }
}
