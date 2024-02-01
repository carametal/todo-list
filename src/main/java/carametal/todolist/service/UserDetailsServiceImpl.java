package carametal.todolist.service;

import carametal.todolist.entity.User;
import carametal.todolist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

public class UserDetailsServiceImpl implements UserDetailsManager {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Assert.hasText(username, "引数\"username\"は1文字以上の英字/数字/記号である必要があります。");
    var user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("username=%sが見つかりませんでした。".formatted(username)));
    return user;
  }

  @Override
  public void createUser(UserDetails userDetails) {
    Assert.isTrue(
        userDetails instanceof User, "引数:userDetailsはUser型である必要があります。:" + userDetails.getClass());
    userRepository.save((User) userDetails);
  }

  @Override
  public void updateUser(UserDetails userDetails) {
    if (userDetails instanceof User user) {
      var userId = user.getId();
      if (userId == null) {
        throw new IllegalArgumentException("user.idがnullになっています。user.idは非Nullである必要があります。");
      }
      var userInRepository = userRepository.findById(userId);
      if (userInRepository.isEmpty()) {
        throw new IllegalArgumentException("リクエストされたユーザーIDは存在しません。ユーザーID:" + userId);
      }
      userInRepository.get().setUsername(user.getUsername());
      return;
    }

    throw new IllegalStateException("引数:userDetailsはUser型である必要があります。:" + userDetails.getClass());
  }

  @Override
  public void deleteUser(String username) {
    var user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("username=%sが見つかりませんでした。".formatted(username));
    }
    var u = user.get();
    if (u == null) {
      throw new IllegalStateException("不明なエラーです。存在すべきuserがnullになっています。");
    }
    userRepository.delete(u);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }
}
