package techcourse.myblog.domain;

import lombok.Builder;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString(exclude = "user")
@Entity
public class SnsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnsInfo snsInfo = (SnsInfo) o;
        return Objects.equals(id, snsInfo.id) &&
                Objects.equals(email, snsInfo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
