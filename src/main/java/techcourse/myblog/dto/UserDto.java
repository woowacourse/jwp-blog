package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    public static class Create {
        private String name;
        private String password;
        private String email;
    }

    @Getter
    @Setter
    public static class Update {
        private String name;
        private String email;
    }
}
