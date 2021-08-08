package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Integer> {
    List<Artist> findByFirstnameAndLastnameAndBirthdate(String firstname, String lastname, String birthdate);
}