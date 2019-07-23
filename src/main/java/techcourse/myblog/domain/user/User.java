package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @Email
    private String email;
    @Pattern(regexp = "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]{8,}$")
    private String password;
    @Pattern(regexp = "^[A-Za-zㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,10}$")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SnsInfo> snsInfos;

    public User() {
    }

    @Builder
    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addSns(String snsEmail, Long snsCode) {
        if (snsInfos == null) {
            snsInfos = new ArrayList();
        }
        snsInfos.add(SnsInfo.builder()
                .snsCode(snsCode)
                .email(snsEmail)
                .user(this).build());
    }

    public String getSnsEmailBySnsCode(long snsCode) {
        Optional<SnsInfo> maybeSnsInfo = getSnsInfo(snsCode);

        if (maybeSnsInfo.isPresent()) {
            return maybeSnsInfo.get().getEmail();
        }
        return "";
    }

    public Optional<SnsInfo> getSnsInfo(long snsCode) {
        return snsInfos.stream()
                .filter(snsInfo -> snsCode == snsInfo.getSnsCode())
                .findFirst();
    }

    public void updateInfo(UserDto userDto) {
        this.name = userDto.getName();
    }

    public void deleteSnsInfo(SnsInfo snsInfo) {
        snsInfos.remove(snsInfo);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", snsInfos=" + snsInfos +
                '}';
    }
}
