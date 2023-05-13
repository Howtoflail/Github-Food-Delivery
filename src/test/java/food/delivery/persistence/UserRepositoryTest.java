package food.delivery.persistence;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace =
AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_shouldSaveUserWithAllFields() {
        UserEntity user = UserEntity.builder().first_name("Marcel")
                .last_name("Baron")
                .email("marcelbaron@yahoo.com")
                .address("Hooghuisstraat 19A")
                .phone("+40736756631")
                .password("Coaiemici2")
                .build();

        UserRoleEntity userRole = UserRoleEntity.builder().user(user).role(RoleEnum.USER).build();
        user.setUserRoles(Set.of(userRole));

        UserEntity savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertNotNull(userRole.getId());

        savedUser = entityManager.find(UserEntity.class, savedUser.getId());
        UserEntity expectedUser = UserEntity.builder()
                .id(savedUser.getId())
                .first_name("Marcel")
                .last_name("Baron")
                .email("marcelbaron@yahoo.com")
                .address("Hooghuisstraat 19A")
                .phone("+40736756631")
                .password("Coaiemici2")
                .build();

        expectedUser.setUserRoles(Set.of(UserRoleEntity.builder().id(userRole.getId()).user(expectedUser).role(RoleEnum.USER).build()));
        assertEquals(expectedUser, savedUser);
    }

    @Test
    void delete_shouldDeleteUser() {
        UserEntity user = UserEntity.builder()
                .first_name("Gabriel")
                .last_name("Versace")
                .email("maman@yahoo.com")
                .address("Keizersgracht 7")
                .phone("+40736756631")
                .password("Ceitreimici1")
                .build();

        user.setUserRoles(Set.of(UserRoleEntity.builder().user(user).role(RoleEnum.USER).build()));

        UserEntity savedUser = userRepository.save(user);
        Long savedUserId = savedUser.getId();
        assertNotNull(savedUserId);

        savedUser = entityManager.find(UserEntity.class, savedUser.getId());
        userRepository.deleteById(savedUser.getId());
        assertNull(entityManager.find(UserEntity.class, savedUserId));
    }
}
