package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {
    @ResponseBody
    @GetMapping("/helloworld")
//    public String temp(@RequestParam(name = "blogName") String blogName) {
    //RequestParam 에서 쿼리의 변수명과 받는 변수명이 같으면 생략가능하다.
    public String temp(String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String asd(@RequestBody String blogName) {
        return blogName;
    }
}
