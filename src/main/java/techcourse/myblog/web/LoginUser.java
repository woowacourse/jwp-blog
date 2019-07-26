package techcourse.myblog.web;

public class LoginUser {
    private String name;
    private String email;

    public LoginUser(String name, String email) {
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
