package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @ToString @EqualsAndHashCode(of = "id")
public class  Article {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String contents;
    @Column
    private String coverUrl;
}
