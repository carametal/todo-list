package carametal.todolist.config;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional
public class BasicAuthenticationProvider implements AuthenticationProvider {

  private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationProvider.class);

  private UserDetailsService userDetailsService;
  private PasswordEncoder passwordEncoder;

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.debug("username:" + authentication.getName());
    logger.debug("password:" + authentication.getCredentials().toString());
    var user = this.userDetailsService.loadUserByUsername(authentication.getName());
    if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(
          user, user.getPassword(), user.getAuthorities());
    }
    throw new BadCredentialsException("ユーザー名とパスワードが一致しません。");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
