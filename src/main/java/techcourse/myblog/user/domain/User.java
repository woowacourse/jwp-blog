package techcourse.myblog.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = EmailConverter.class)
    private Email email;

    @Convert(converter = PasswordConverter.class)
    private Password password;

    @Convert(converter = NameConverter.class)
    private Name name;

    @Builder
    private User(long id, String email, String password, String name) {
        this.id = id;
        this.email = Email.of(email);
        this.password = Password.of(password);
        this.name = Name.of(name);
    }

    public void update(String name) {
        this.name = this.name.updateName(name);
    }

    public boolean checkPassword(String password) {
        return this.password.isCorrect(password);
    }
}
