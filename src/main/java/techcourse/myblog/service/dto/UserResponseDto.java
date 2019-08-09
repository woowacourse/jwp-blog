package techcourse.myblog.service.dto;

import javax.validation.constraints.Pattern;

public class UserResponseDto {
    private static final String NAME_PATTERN = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$";

    @Pattern(regexp = NAME_PATTERN)
    private String name;
    private String email;

    public UserResponseDto() {
    }

    public UserResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
