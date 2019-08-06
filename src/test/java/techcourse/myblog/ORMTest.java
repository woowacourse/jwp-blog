package techcourse.myblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ORMTest {
    @Autowired
    private _UserRepository _userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findById() {
        _User _user = new _User("Donut");
        _User _persistentUser = testEntityManager.persist(_user);
        _Article _article = new _Article("Jemok", "Naeyong");
        _article.setAuthor(_persistentUser);
        testEntityManager.persist(_article);
        testEntityManager.flush();
        testEntityManager.clear();
        _User _actualUser = _userRepository.findById(_persistentUser.getId()).get();
        assertThat(_actualUser.getArticles().size()).isEqualTo(1);
    }

    @Test
    public void findById2() {
        _Article _article = new _Article("Zemoc", "MyDragon");
        _Article _persistentArticle = testEntityManager.persist(_article);
        _User user = new _User("Donatsu");
        user.addArticle(_persistentArticle);
        _User _persistentUser = testEntityManager.persist(user);
        testEntityManager.flush();
        testEntityManager.clear();
        _User _actualUser = _userRepository.findById(_persistentUser.getId()).get();
        assertThat(_actualUser.getArticles().size()).isEqualTo(0);
    }
}

@Entity
class _User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "author")
    private List<_Article> articles = new ArrayList<>();

    public _User() {}

    public _User(String name) {
        this.name = Optional.ofNullable(name).orElse("name");
    }

    public void addArticle(_Article article) {
        this.articles.add(article);
    }

    public long getId() {
        return this.id;
    }

    public List<_Article> getArticles() {
        return this.articles;
    }
}

@Entity
class _Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private _User author;

    public _Article() {}

    public _Article(String title, String content) {
        this.title = Optional.ofNullable(title).orElse("title");
        this.content = Optional.ofNullable(content).orElse("content");
    }

    public void setAuthor(_User author) {
        this.author = author;
    }
}

@Repository
interface _UserRepository extends CrudRepository<_User, Long> {
    Optional<_User> findById(long id);
}