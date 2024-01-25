package carametal.todolist.controller;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class UserController {
  @PostMapping
  public void registerUser() {
    return;
  }
}
