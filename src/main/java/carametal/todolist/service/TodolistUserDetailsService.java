package carametal.todolist.service;

import carametal.todolist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TodolistUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public TodolistUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findById(username).orElseThrow();
    return user;
  }
}
