package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String coverUrl;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public boolean isWrittenBy(User user) {
        return author.equals(user);
    }
}