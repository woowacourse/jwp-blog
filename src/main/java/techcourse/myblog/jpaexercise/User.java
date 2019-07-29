//package techcourse.myblog.jpaexercise;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//public class User {
//
//    @Id
//    @GeneratedValue
//    private long id;
//
//    private String name;
//    private String email;
//    private String password;
//
//    public User() {}
//
//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY) // fetch 기본값
////    @JoinColumn(name = "article_id")
//    private List<Article> articles;
//
//    public long getId() {
//        return id;
//    }
//
//    public String getUserName() {
//        return name;
//    }
//
//    public void setUserName(final String userName) {
//        this.name = userName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(final String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(final String password) {
//        this.password = password;
//    }
//
//    public List<Article> getArticles() {
//        return articles;
//    }
//}