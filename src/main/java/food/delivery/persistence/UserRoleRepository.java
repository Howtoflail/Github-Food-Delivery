package food.delivery.persistence;

import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findTop1ByRoleOrderByIdDesc(RoleEnum role);

    @Modifying
    @Query(value = "DELETE user_roles, users FROM user_roles, users WHERE user_roles.user_id = ?1 AND users.id = ?1", nativeQuery = true)
    void deleteByUser_Id(Long userId);
}
