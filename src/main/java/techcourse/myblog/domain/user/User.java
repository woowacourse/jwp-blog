package techcourse.myblog.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @Column(nullable = false)
    private UserName name;

    @Embedded
    @Column(nullable = false)
    private UserPassword password;

    @Embedded
    @Column(unique = true)
    private UserEmail email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    protected User() {
    }

    public User(String name, String password, String email) {
        this.name = UserName.of(name);
        this.password = UserPassword.of(password);
        this.email = UserEmail.of(email);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getUserName();
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public User updateNameAndEmail(String name, String email) {
        this.name = UserName.of(name);
        this.email = UserEmail.of(email);
        return this;
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(UserPassword.of(password));
    }

    public boolean isMatchEmail(String email) {
        return this.email.equals(UserEmail.of(email));
    }

    public boolean isMatchEmail(User user) {
        return this.email.equals(user.email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addArticle(Article article) {
        articles.add(article);
    }
}
