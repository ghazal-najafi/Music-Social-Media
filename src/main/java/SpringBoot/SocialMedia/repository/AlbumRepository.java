package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Album;
import SpringBoot.SocialMedia.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {
    Album findByName(String name);
}