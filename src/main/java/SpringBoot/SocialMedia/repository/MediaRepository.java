package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Media;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MediaRepository extends CrudRepository<Media, Integer> {
    @Query(value = "select * from media where albumid=?", nativeQuery = true)
    List<Media> findByAlbumID(int id);

}


