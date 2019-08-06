package techcourse.myblog.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String userName;
    @NonNull
    @Column(nullable = false)
    private String email;
    @NonNull
    @Column(nullable = false)
    private String password;

    public boolean authenticate(String checkPassword) {
        return this.password.equals(checkPassword);
    }
}
