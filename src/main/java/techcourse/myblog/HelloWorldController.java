package techcourse.myblog;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {
	@GetMapping("/helloworld")
	public String getBlogNameUsingGet(String blogName) {
		return blogName;
	}

	@PostMapping("/helloworld")
	public String getBlogNameUsingPost(@RequestBody String blogName) {
		return blogName;
	}

	@GetMapping("/{blogName}")
	public String changeBlogName(@PathVariable("blogName") String name) { //@PathVariable
		return name;
	}
}
