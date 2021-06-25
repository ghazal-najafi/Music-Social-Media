package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}


