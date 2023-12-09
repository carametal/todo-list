package carametal.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = {"classpath:sql/insert_testuser.sql"})
@Sql(
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
    scripts = {"classpath:sql/truncate.sql"})
public class HelloWorldControllerTest {

  @Autowired private MockMvc mockMvc;
  public static final String URI = "/hello-world";
  private static final Encoder encoder = Base64.getEncoder();

  private static String getEncoded(String text) {
    return "Basic " + encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void Authenticationヘッダーに正しいユーザー名とパスワードがある場合は200を返す() throws Exception {
    mockMvc
        .perform(get(URI).header(HttpHeaders.AUTHORIZATION, getEncoded("testuser:testuser")))
        .andExpect(status().isOk());
  }

  @Test
  void Authorizationヘッダーがない場合は401を返す() throws Exception {
    mockMvc.perform(get(URI)).andExpect(status().isUnauthorized());
  }

  @Test
  void ユーザー名がデータベースに存在しない場合は401を返す() throws Exception {
    mockMvc
        .perform(get(URI).header(HttpHeaders.AUTHORIZATION, getEncoded("wrongUser:testuser")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void ユーザー名がデータベースに存在するがパスワードが一致しない場合は401を返す() throws Exception {
    mockMvc
        .perform(get(URI).header(HttpHeaders.AUTHORIZATION, getEncoded("testuser:wrongPassword")))
        .andExpect(status().isUnauthorized());
  }
}
