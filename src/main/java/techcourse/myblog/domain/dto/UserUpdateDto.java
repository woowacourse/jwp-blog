package techcourse.myblog.domain.dto;

public class UserUpdateDto {
    private long id;
    private String name;
    private String snsGithub;
    private String snsFacebook;

    public UserUpdateDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnsGithub() {
        return snsGithub;
    }

    public void setSnsGithub(String snsGithub) {
        this.snsGithub = snsGithub;
    }

    public String getSnsFacebook() {
        return snsFacebook;
    }

    public void setSnsFacebook(String snsFacebook) {
        this.snsFacebook = snsFacebook;
    }
}
