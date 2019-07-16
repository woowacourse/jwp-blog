package techcourse.myblog.domain;

public class UserRequestDto {
    private String name;
    private String email;
    private String password;

    public UserRequestDto() {
    }

    public UserRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserRequestDto from(User user) {
        return new UserRequestDto(user.getName(), user.getEmail(), user.getPassword());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser() {
        return new User(name, email, password);
    }
}
