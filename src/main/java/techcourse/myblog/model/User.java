package techcourse.myblog.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_NAME", nullable = false, length = 30)
    private String userName;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;
}
