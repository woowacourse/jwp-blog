package techcourse.myblog.application.dto;

import techcourse.myblog.domain.User;

public class UserResponseDto extends UserDto {
    private Long id;

    public UserResponseDto(Long id, String email, String name) {
        super(email, name);
        this.id = id;
    }

    public Boolean match(User user) {
        if(user == null)
            return false;
        return super.match(user) && id.equals(user.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}