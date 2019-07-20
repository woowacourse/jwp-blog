package study.thymeleaf;

public class TsUser {
    private String name;

    private String email;

    private int age;

    public TsUser(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
