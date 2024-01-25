package carametal.todolist.config;

import carametal.todolist.repository.UserRepository;
import carametal.todolist.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserRepository userRepository;

  public SecurityConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsManager userDetailsManager) {
    var basicAuthenticationProvider = new BasicAuthenticationProvider();
    basicAuthenticationProvider.setUserDetailsService(userDetailsManager);
    basicAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return basicAuthenticationProvider;
  }

  @Bean
  public UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
    return new UserDetailsServiceImpl(userRepository, passwordEncoder);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
