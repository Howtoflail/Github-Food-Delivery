package food.delivery.business.impl;

import food.delivery.business.CreateRestaurantUseCase;
import food.delivery.business.exception.RestaurantNameAlreadyExistsException;
import food.delivery.business.exception.UserEmailAlreadyExistsException;
import food.delivery.domain.CreateRestaurantRequest;
import food.delivery.domain.CreateRestaurantResponse;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.UserRoleRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private RestaurantRepository restaurantRepository;
    private UserRoleRepository userRoleRepository;
    private UserRepository userRepository;

    @Override
    @Transactional
    public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        if(restaurantRepository.findByName(request.getName()) != null){
            throw new RestaurantNameAlreadyExistsException();
        }

        RestaurantEntity savedRestaurant = saveNewRestaurant(request);

        return CreateRestaurantResponse.builder().restaurantId(savedRestaurant.getId()).build();
    }

//    private void store(MultipartFile file){
//        //storing the image
//        //this returns D:\programe\GitLab\dragos-food-delivery
//        Path rootLocation = Paths.get("").toAbsolutePath();
//
//        try {
//            if(file.isEmpty()){
//                throw new RuntimeException("Failed to store an empty file!");
//            }
//            Path destinationFile = rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
//        }
//        catch (Exception e){
//            throw new RuntimeException("Failed to store the file.", e);
//        }
//    }

    private RestaurantEntity saveNewRestaurant(CreateRestaurantRequest request){
        UserRoleEntity userRoleEntity = userRoleRepository.findTop1ByRoleOrderByIdDesc(RoleEnum.RESTAURANT);

        RestaurantEntity newRestaurant = RestaurantEntity.builder()
                .name(request.getName())
                .user(userRoleEntity.getUser())
                .type(request.getType())
                .address(request.getAddress())
                .build();

        return restaurantRepository.save(newRestaurant);
    }
}
