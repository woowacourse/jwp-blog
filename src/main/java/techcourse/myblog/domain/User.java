package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    public static final String PASSWORD_PATTERN = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String NAME_PATTERN = "^.{2,10}$";
    public static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    public User(String name, String email, String password) {
        if (name.length() > 10 || name.length() < 2) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isSameMail(String email) {
        return this.email==email;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }
}