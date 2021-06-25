package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

}