package carametal.todolist.controller;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class HelloWorldController {
  @GetMapping("/hello-world")
  public String helloWorld() {
    return "Hello World.";
  }

  @GetMapping("/hello-user")
  public String helloUser() {
    var context = SecurityContextHolder.getContext();
    var authentication = context.getAuthentication();
    return "Hello " + authentication.getName() + ".";
  }
}
