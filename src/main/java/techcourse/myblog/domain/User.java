package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class User {
    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
}

