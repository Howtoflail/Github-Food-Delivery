package food.delivery.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String first_name;

    @Column
    @NotBlank
    private String last_name;

    @Column
    @NotBlank
    private String email;

    @Column
    @NotBlank
    private String address;

    @Column
    @NotBlank
    private String phone;

    @Column
    @NotBlank
//    @Size(min = 6, max = 20, message = "criteria not met")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<UserRoleEntity> userRoles;
}
