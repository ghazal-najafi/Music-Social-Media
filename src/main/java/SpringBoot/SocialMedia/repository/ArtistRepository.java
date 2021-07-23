package SpringBoot.SocialMedia.repository;

import SpringBoot.SocialMedia.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Integer> {
    public List<Artist> findByFirstnameAndLastnameAndBirthdate(String firstname, String lastname, String birthdate);

}