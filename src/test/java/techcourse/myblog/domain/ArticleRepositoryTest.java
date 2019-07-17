package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//SpringBoot환경과 같은 환경을 사용한다느,ㄴ 의미에서 이 어노테이션을 사용한다
//이게 없으면 그냥 컴포넌트 스캔과 같은 부분이 없다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest {
    //포비 :
    //컨트롤러 테스트에서 Repo에 의존관계를 맺고 있는 경우가 많다.
    //리포지토리를 테스트가 의존 관계에 있으면 영향을 받고 있다.
    //컨트롤러는 Http 클라이언트라서 레포랑 의존을 맺지 않는게 좋다
    //그렇게 해야 이번처럼 변경이 되었을때
    //바로 할 수 있다.

    //이렇게 했는데 왜 NullPointer가 뜨지 ? 실제로 사용할 땐 사용이 되지만 왜 테스트에서는
    // ...?
    //널이 뜨는 사람은 Test위에 SpringBootTest라는 환경을 설정해야 된다.
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void 저장_확인() {
        Article article = new Article("title", "Url", "contents");
        article.setId(1L);
        Article newArticle = articleRepository.save(article);
        assertThat(articleRepository.findById(1L).get()).isEqualTo(article);
    }
//
//    @Test
//    void 삭제_확인() {
//        Article article = new Article("title", "Url", "contents");
//        articleRepository.save(article);
//        articleRepository.deleteById(0L);
//        assertThrows(IllegalArgumentException.class, () -> articleRepository.findById(0L));
//    }
}