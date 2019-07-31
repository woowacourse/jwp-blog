package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 100)
    private String userName;

    @NonNull
    @Column(length = 100)
    private String email;

    @NonNull
    @Column(length = 100)
    private String password;


    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void update(User user) {
        this.userName = user.userName;
        this.email = user.email;
        this.password = user.password;
    }

    public boolean isAuthorOf(Article savedArticle) {
        return this.equals(savedArticle.getAuthor());
    }
}
