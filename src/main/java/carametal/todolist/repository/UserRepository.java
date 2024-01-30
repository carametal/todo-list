package carametal.todolist.repository;

import carametal.todolist.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  public Optional<User> findByUsername(String username);

  public void deleteByUsername(String username);
}
