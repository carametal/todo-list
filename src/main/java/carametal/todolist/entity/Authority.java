package carametal.todolist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@Table(
    name = "authorities",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})})
public class Authority implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer userId;

  @Column(length = 60)
  private String authority;

  @Override
  public String getAuthority() {
    return authority;
  }
}
