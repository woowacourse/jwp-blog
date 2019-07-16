package techcourse.myblog.web;

public class UserRequestDto {
    private String userName;
    private String email;
    private String password;
    private String passwordConfirm;

    public static UserRequestDto of(String userName, String email, String password, String passwordConfirm) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.userName = userName;
        userRequestDto.email = email;
        userRequestDto.password = password;
        userRequestDto.passwordConfirm = passwordConfirm;
        return userRequestDto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
