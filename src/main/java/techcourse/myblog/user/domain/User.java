package techcourse.myblog.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.user.domain.vo.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = EmailConverter.class)
    private Email email;

    @Convert(converter = PasswordConverter.class)
    @JsonIgnore
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

    public User update(String name) {
        this.name = this.name.updateName(name);
        return this;
    }

    public boolean checkId(long id) {
        return this.id == id;
    }

    public String userName() {
        return name.getName();
    }
    public boolean checkPassword(String password) {
        return this.password.isCorrect(password);
    }
}
