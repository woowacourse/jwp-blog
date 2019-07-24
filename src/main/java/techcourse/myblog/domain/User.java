package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.support.validation.UserGroups;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$",
            message = "이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없습니다.",
            groups = {UserGroups.Edit.class, UserGroups.All.class})
    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합입니다.")
    private String password;

    private User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User from(String name, String email, String password) {
        return new User(name, email, password);
    }

    public void modify(User user) {
        this.name = user.name;
    }
}
