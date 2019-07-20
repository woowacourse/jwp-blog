package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = {"id"})
@ToString
@Data
@NoArgsConstructor
public class User {
    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;
    private String email;
}
