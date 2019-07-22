package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.ToString;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.Email;

@ToString(exclude = "user")
@Entity
public class SnsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public SnsInfo() {
    }

    @Builder
    public SnsInfo(String email, User user) {
        this.email = email;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }
}
