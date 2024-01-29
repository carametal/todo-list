package carametal.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import carametal.todolist.AbstractDbTest;
import carametal.todolist.entity.User;
import carametal.todolist.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = {"classpath:sql/truncate.sql", "classpath:sql/insert_testuser.sql"})
@Transactional
public class UserDetailsServiceImplTest extends AbstractDbTest {

  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  public UserDetailsServiceImplTest(
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository, passwordEncoder);
  }

  @Nested
  class ユーザー名によるユーザー存在確認 {
    @ParameterizedTest
    @ValueSource(strings = {"testadmin", "testuser"})
    void DBに存在するusernameを渡すとUserDetailsオブジェクトを返す(String input) {
      var user = userDetailsServiceImpl.loadUserByUsername(input);
      assertEquals(user.getUsername(), input);
    }

    @Test
    void DBに存在しないusernameを渡すとUsernameNotFoundExceptionを投げる() {
      assertThrows(
          UsernameNotFoundException.class,
          () -> {
            userDetailsServiceImpl.loadUserByUsername("not-exists-user");
          });
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "　"})
    @Tag("learning")
    void 不正な値を渡すとIllegalArgumentExceptionを投げる(String input) {
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            userDetailsServiceImpl.loadUserByUsername(input);
          });
    }
  }

  @Nested
  class ユーザー登録 {
    @Test
    void 新規ユーザーを登録できる() {
      var user = new User();
      user.setUsername("user");
      user.setPassword("password");
      assertDoesNotThrow(
          () -> {
            userDetailsServiceImpl.createUser(user);
          });
    }

    @Test
    void すでに存在しているユーザー名を指定した場合は例外を投げる() {
      var user = new User();
      user.setUsername("testuser");
      user.setPassword("password");
      assertThrows(
          DataIntegrityViolationException.class,
          () -> {
            userDetailsServiceImpl.createUser(user);
          });
    }
  }
}
