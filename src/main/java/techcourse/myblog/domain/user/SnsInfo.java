package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.Email;

@ToString(exclude = "user")
@Getter
@Entity
public class SnsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    private String email;
    private long snsCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_sns_to_user"))
    private User user;

    public SnsInfo() {
    }

    @Builder
    public SnsInfo(String email, User user, long snsCode) {
        this.email = email;
        this.user = user;
        this.snsCode = snsCode;
    }

    public void updateSnsInfo(SnsInfo snsInfo) {
        this.email = snsInfo.getEmail();
    }
}
