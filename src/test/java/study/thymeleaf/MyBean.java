package study.thymeleaf;

public class MyBean {
    private String name;

    private String email;

    public MyBean(String name, String email) {
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
