package techcourse.myblog.user.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.domain.vo.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Builder
    private User(long id, String email, String password, String name) {
        this.id = id;
        this.email = Email.of(email);
        this.password = Password.of(password);
        this.name = Name.of(name);
    }

    public User update(String name) {
        this.name = this.name.updateName(name);
        return this;
    }

    public boolean isPassword(String password) {
        return this.password.isCorrect(password);
    }

    public void checkMatchUser(long id) {
        if (this.id != id) {
            throw new NotMatchUserException();
        }
    }
}

