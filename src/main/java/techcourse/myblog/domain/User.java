package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.domain.userinfo.UserEmail;
import techcourse.myblog.domain.userinfo.UserName;
import techcourse.myblog.domain.userinfo.UserPassword;
import techcourse.myblog.dto.UserEditRequestDto;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 8)
    private UserName name;

    @Column(nullable = false, unique = true, length = 50)
    private UserEmail email;

    @Column(nullable = false)
    private UserPassword password;

    @Builder
    public User(String name, String email, String password) {
        this.name = new UserName(name);
        this.email = new UserEmail(email);
        this.password = new UserPassword(password);
    }

    public void update(UserEditRequestDto userEditRequestDto) {
        this.name = new UserName(userEditRequestDto.getName());
    }

    public boolean matchName(String name) {
        return this.name.match(name);
    }

    public boolean matchName(UserName userName) {
        return this.name.equals(userName);
    }

    public boolean matchEmail(String email) {
        return this.email.match(email);
    }

    public boolean matchEmail(UserEmail userEmail) {
        return this.email.equals(userEmail);
    }

    public boolean matchPassword(String password) {
        return this.password.match(password);
    }

    public boolean matchPassword(UserPassword userPassword) {
        return this.password.equals(userPassword);
    }
}
