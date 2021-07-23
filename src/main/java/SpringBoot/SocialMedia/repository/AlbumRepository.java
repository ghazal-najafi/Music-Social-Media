package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Album;
import SpringBoot.SocialMedia.model.User;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Integer> {
    public Album findByName(String name);
}