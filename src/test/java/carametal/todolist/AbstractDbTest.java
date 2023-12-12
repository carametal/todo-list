package carametal.todolist;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class AbstractDbTest {
  @Container
  private static PostgreSQLContainer postgres =
      new PostgreSQLContainer()
          .withDatabaseName("todo_list")
          .withUsername("postgres")
          .withPassword("postgres");

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    // registry.add("spring.datasource.username", postgres::getUsername);
    // registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void テスト用postgresコンテナが実行されている() {
    assertTrue(postgres.isRunning());
  }
}
