package learningtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import techcourse.myblog.repository.ArticleRepository;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Mockito2Test {

    @Mock
    ArticleRepository articleRepository;

    @Test
    public void mockTest() {

        when(articleRepository.findById(1L)).thenReturn(null);

        System.out.println(articleRepository.findById(1L));
    }
}
