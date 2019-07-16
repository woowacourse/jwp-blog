package techcourse.myblog.web;

public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private String passwordConfirm;

    public static UserRequestDto of(String userName, String email, String password, String passwordConfirm) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.name = userName;
        userRequestDto.email = email;
        userRequestDto.password = password;
        userRequestDto.passwordConfirm = passwordConfirm;
        return userRequestDto;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
