//package jpaexercise;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@DataJpaTest
//class UserRepositoryTests {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @Test
//    void findById() {
//        User user = new User();
//        User persistUser = testEntityManager.persist(user);
//
//        Article article = new Article();
//        article.setAuthor(persistUser);
//        testEntityManager.persist(article);
//
//        testEntityManager.flush();
//        testEntityManager.clear();
//
//        User actualUser = userRepository.findById(persistUser.getId()).get();
//
//        assertThat(actualUser.getArticles().size()).isEqualTo(1);
//    }
//
////    @Test
////    void findById2() {
////        Article article = new Article();
////        Article persistArticle = testEntityManager.persist(article);
////
////        User user = new User();
////        user.addArticle(persistArticle);
////        User persistUser = testEntityManager.persist(user);
////
////        testEntityManager.flush();
////        testEntityManager.clear();
////
////        User actualUser = userRepository.findById(persistUser.getId()).get();
////
////        assertThat(actualUser.getArticles().size()).isEqualTo(0);
////    }
//
//}