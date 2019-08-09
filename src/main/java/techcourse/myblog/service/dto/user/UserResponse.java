package techcourse.myblog.service.dto.user;

import java.util.Objects;

public class UserResponse {
    private Long id;
    private String email;
    private String name;

    public UserResponse(final Long id, final String email, final String name) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);

        this.id = id;
        this.email = email;
        this.name = name;
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
        UserResponse that = (UserResponse) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }
}
