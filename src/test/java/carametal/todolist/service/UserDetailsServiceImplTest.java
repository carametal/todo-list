package carametal.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

  @Nested
  class ユーザー削除 {
    @Test
    void ユーザーが削除されている() {
      assertNotNull(userDetailsServiceImpl.loadUserByUsername("testuser"));
      userDetailsServiceImpl.deleteUser("testuser");
      assertThrows(
          UsernameNotFoundException.class,
          () -> {
            userDetailsServiceImpl.loadUserByUsername("testuser");
          });
    }

    @Test
    @Tag("learning")
    void 存在しないユーザー名を指定するとUsernameNotFoundExceptionを投げる() {
      assertThrows(
          UsernameNotFoundException.class,
          () -> {
            userDetailsServiceImpl.deleteUser("notExistsUser");
          });
    }
  }

  @Nested
  class ユーザー存在確認 {
    @Test
    void ユーザー名が存在する場合はtrueを返す() {
      assertTrue(userDetailsServiceImpl.userExists("testuser"));
    }

    @Test
    void ユーザー名が存在しない場合はfalseを返す() {
      assertFalse(userDetailsServiceImpl.userExists("not-exists-user"));
    }
  }

  @Nested
  class ユーザー更新 {
    @Test
    void ユーザーを更新できる() {
      var user = (User) userDetailsServiceImpl.loadUserByUsername("testuser");
      user.setUsername("new-test-user");
      userDetailsServiceImpl.updateUser(user);
      var updatedUser = userDetailsServiceImpl.loadUserByUsername("new-test-user");
      assertEquals("new-test-user", updatedUser.getUsername());
    }

    @Test
    void ユーザーIDがnullだとIllegalArgumentExceptionを投げる() {
      var user = new User();
      user.setId(null);
      user.setUsername("new-test-user");
      user.setPassword("password");
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            userDetailsServiceImpl.updateUser(user);
          });
    }

    @Test
    void 存在しないユーザーIDを渡すとIllegalArgumentExceptionを投げる() {
      var user = new User();
      user.setId(99);
      user.setUsername("new-test-user");
      user.setPassword("password");
      assertThrows(
          IllegalArgumentException.class,
          () -> {
            userDetailsServiceImpl.updateUser(user);
          });
    }
  }
}
