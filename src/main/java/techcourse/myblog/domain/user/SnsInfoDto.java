package techcourse.myblog.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnsInfoDto {
    private String email;
    private long snsCode;

    @Builder
    public SnsInfoDto(String email, long snsCode) {
        this.email = email;
        this.snsCode = snsCode;
    }


}
