package study.thymeleaf;

public class MyBean {
    private String name;

    private String email;

    private Integer age;

    public MyBean(String name, String email) {
        this(name, email, null);
    }

    public MyBean(String name, String email, Integer age) {
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

    public Integer getAge() {
        return age;
    }
}
