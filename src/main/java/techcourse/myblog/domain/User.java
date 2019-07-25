package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String userName;
    @NonNull
    private String email;
    @NonNull
    private String password;

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void update(User user) {
        this.userName = user.userName;
        this.email = user.email;
        this.password = user.password;
    }
}
