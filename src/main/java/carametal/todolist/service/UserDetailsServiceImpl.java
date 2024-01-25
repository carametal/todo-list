package carametal.todolist.service;

import carametal.todolist.entity.User;
import carametal.todolist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

public class UserDetailsServiceImpl implements UserDetailsManager {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("username=%sが見つかりませんでした。".formatted(username)));
    return user;
  }

  @Override
  public void createUser(UserDetails userDetails) {
    if (userDetails instanceof User user) {
      userRepository.save(user);
    }
    throw new IllegalStateException("引数:userDetailsはUser型である必要があります。");
  }

  @Override
  public void updateUser(UserDetails user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
  }

  @Override
  public void deleteUser(String username) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

  @Override
  public boolean userExists(String username) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'userExists'");
  }
}
