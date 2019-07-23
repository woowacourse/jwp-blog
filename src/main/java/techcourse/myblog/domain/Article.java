package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", length = 10, nullable = false)
    private String title;

    @Column(name = "contents", length = 100, nullable = false)
    private String contents;

    @Column(name = "coverUrl", length = 60, nullable = false)
    private String coverUrl;
}
