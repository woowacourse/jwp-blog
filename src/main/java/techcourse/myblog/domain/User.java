package techcourse.myblog.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Column(nullable = false)
    private String password;

    @Transient
    private static final String namePattern = "^[^ \\-!@#$%^&*(),.?\\\":{}|<>0-9]{2,10}$";
    @Transient
    private static final String emailPattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    @Transient
    private static final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$";

    private User() {
    }

    public User(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (isEmpty(name) || !Pattern.matches(namePattern, name)) {
            throw new IllegalUserException("이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (isEmpty(email) || !Pattern.matches(emailPattern, email)) {
            throw new IllegalUserException("이메일 양식을 지켜주세요.");
        }
    }

    private void validatePassword(String password) {
        if (isEmpty(password) || !Pattern.matches(passwordPattern, password)) {
            throw new IllegalUserException("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.");
        }
    }

    private boolean isEmpty(String text) {
        return (text == null) || ("").equals(text);
    }

    public User modifyName(String name) {
        validateName(name);
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}