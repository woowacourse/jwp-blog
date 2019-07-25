package techcourse.myblog.service.dto;

public class UserUpdateDto {
    private Long id;
    private String name;

    public UserUpdateDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
