package techcourse.myblog.web.dto;

import techcourse.myblog.domain.User;

import java.util.Objects;

public class UserDto {
    private Long id;
    private String email;
    private String name;

    public UserDto(final Long id, final String email, final String name) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);

        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserDto from(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String name = user.getName();
        return new UserDto(id, email, name);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto that = (UserDto) o;
        return Objects.equals(email, that.email) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }
}
