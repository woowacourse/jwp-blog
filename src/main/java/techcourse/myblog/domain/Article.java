package techcourse.myblog.domain;


import lombok.*;
import techcourse.myblog.application.dto.ArticleDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String title;

    @Column
    @NotBlank
    private String coverUrl;

    @Column
    @NotBlank
    private String contents;

    public void update(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.contents = articleDto.getContents();
        this.coverUrl = articleDto.getCoverUrl();
    }
}
