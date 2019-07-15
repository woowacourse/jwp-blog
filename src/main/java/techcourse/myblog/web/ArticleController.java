package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

//TODO 메서드명에 대해서 알아보기 메서드 명에 Article과 같은게 들어가는게 더 나은지
//없애는게 깔끔한 것 같다. 클래스명에서 의미 전달이 된다고 생각하기때문에
//repo도 그러면 saveARTICLE이였..
//아니면 이 상태로 괜찮은지?
@Controller
public class ArticleController {

    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    //TODO ModelAndView와 Model로 하고 String을 넘길때는 어떤 차이가 있나?
    //차이가 없고, 다만 ModelAndView가 옛날거다.
    //명확해서 쓰기는 쓰시지만 팀이나 취향에 따라 다르다.
    //파라미터에서 받지 말고 안에서 새로 생성하자.
    //파라미터에서 모델엔뷰 안에 모델이 있어서 작동은 하는데,
    //정상적인 케이스는 안에서 새로 만들어 주는게 더 낫다.
    @GetMapping("/")
    public ModelAndView index(String blogName) {
        if (blogName == null) {
            blogName = "누구게?";
        }
        //아래와 같이 하지말고 findAll()을 한 뒤에 던져주자.
//        modelAndView.setViewName("index");
//        modelAndView.addObject("blogName", blogName);
//        modelAndView.addObject("articleRepository", articleRepository);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("blogName", blogName);
//        List<Article> articles = articleRepository.findAll();
        modelAndView.addObject("articles", articleRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/writing")
    public String writeForm() {
        return "article-edit";
    }

    //TODO 여기는 annotation은 어딧지? @ResponseBody는 언제 붙이고 이때는 왜 생략해야될까?
    @PostMapping("/articles")
    public ModelAndView save(Article article, ModelAndView modelAndView) {
        articleRepository.save(article);
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/article/{articleId}")
    public ModelAndView show(@PathVariable String articleId, ModelAndView modelAndView) {
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView writeForm(@PathVariable String articleId, ModelAndView modelAndView) {
        modelAndView.setViewName("article-edit");
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    //path에 담는 이유는 REstful한 무엇인가를 하게 보이기 위한? 그런 느낌이지 않으띾?
    //이렇게 담을건지 안담을 건지는 취향차이이다.
    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable String articleId, Article article) {
        articleRepository.updateArticle(Integer.parseInt(articleId), article);
        return "redirect:/";
    }

    @DeleteMapping("/articles/{articleId}")
    public String delete(@PathVariable String articleId) {
        articleRepository.removeById(Integer.parseInt(articleId));
        return "redirect:/";
    }
}
