package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "contents", length = 6000, nullable = false)
    private String contents;

    @Column(name = "coverUrl", length = 1000, nullable = false)
    private String coverUrl;
}
