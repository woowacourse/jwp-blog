package learningtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Mockito3Test {
    @Mock
    ArticleRepository articleRepository;

    @Test
    public void hasLocalMockInThisTest(@Mock List<Integer> localList) {
        localList.add(100);
    }

    @Test
    void asd() {
        when(articleRepository.findById(1L)).thenReturn(null);

        System.out.println(articleRepository.findById(1L));
    }
}
