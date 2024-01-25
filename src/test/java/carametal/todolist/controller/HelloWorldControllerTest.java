package carametal.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import carametal.todolist.AbstractDbTest;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.junit.jupiter.api.Nested;
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
    scripts = {"classpath:sql/truncate.sql", "classpath:sql/insert_testuser.sql"})
public class HelloWorldControllerTest extends AbstractDbTest {

  @Autowired private MockMvc mockMvc;
  public static final String URI_HELLO_WORLD = "/hello-world";
  public static final String URI_HELLO_USER = "/hello-user";
  private static final Encoder encoder = Base64.getEncoder();

  private static String getEncoded(String text) {
    return "Basic " + encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
  }

  @Nested
  class HelloWorld {
    @Test
    void Authenticationヘッダーに正しいユーザー名とパスワードがある場合は200を返す() throws Exception {
      mockMvc
          .perform(
              get(URI_HELLO_WORLD)
                  .header(HttpHeaders.AUTHORIZATION, getEncoded("testuser:testuser")))
          .andExpect(status().isOk());
    }

    @Test
    void Authorizationヘッダーがない場合は401を返す() throws Exception {
      mockMvc.perform(get(URI_HELLO_WORLD)).andExpect(status().isUnauthorized());
    }

    @Test
    void ユーザー名がデータベースに存在しない場合は401を返す() throws Exception {
      mockMvc
          .perform(
              get(URI_HELLO_WORLD)
                  .header(HttpHeaders.AUTHORIZATION, getEncoded("wrongUser:testuser")))
          .andExpect(status().isUnauthorized());
    }

    @Test
    void ユーザー名がデータベースに存在するがパスワードが一致しない場合は401を返す() throws Exception {
      mockMvc
          .perform(
              get(URI_HELLO_WORLD)
                  .header(HttpHeaders.AUTHORIZATION, getEncoded("testuser:wrongPassword")))
          .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  class HelloUser {
    @Test
    void testadminの認証情報でリクエストするとHello_testadminが返ってくる() throws Exception {
      mockMvc
          .perform(
              get(URI_HELLO_USER)
                  .header(HttpHeaders.AUTHORIZATION, getEncoded("testadmin:testadmin")))
          .andExpect(status().isOk())
          .andExpect(content().string("Hello testadmin."));
    }

    @Test
    void testuserの認証情報でリクエストするとHello_testuserが返ってくる() throws Exception {
      mockMvc
          .perform(
              get(URI_HELLO_USER)
                  .header(HttpHeaders.AUTHORIZATION, getEncoded("testuser:testuser")))
          .andExpect(status().isOk())
          .andExpect(content().string("Hello testuser."));
    }
  }
}
