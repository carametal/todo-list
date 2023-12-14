package carametal.todolist.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TodoListConfig {
  @Bean
  @Profile("test")
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    FlywayMigrationStrategy strategy =
        flyway -> {
          flyway.migrate();
        };
    return strategy;
  }
}
