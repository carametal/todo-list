package carametal.todolist.config;

import carametal.todolist.service.TodolistUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final TodolistUserDetailsService todolistUserDetailsService;

  // private final BasicAuthenticationProvider basicAuthenticationProvider;

  public SecurityConfig(TodolistUserDetailsService todolistUserDetailsService) {
    this.todolistUserDetailsService = todolistUserDetailsService;
    // this.basicAuthenticationProvider = basicAuthenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    var basicAuthenticationProvider = new BasicAuthenticationProvider();
    basicAuthenticationProvider.setUserDetailsService(todolistUserDetailsService);
    basicAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return basicAuthenticationProvider;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return todolistUserDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
