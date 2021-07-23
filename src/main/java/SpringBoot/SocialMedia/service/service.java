package SpringBoot.SocialMedia.service;

import SpringBoot.SocialMedia.model.Album;
import SpringBoot.SocialMedia.model.Artist;
import SpringBoot.SocialMedia.model.Media;
import SpringBoot.SocialMedia.model.User;
import SpringBoot.SocialMedia.repository.AlbumRepository;
import SpringBoot.SocialMedia.repository.ArtistRepository;
import SpringBoot.SocialMedia.repository.MediaRepository;
import SpringBoot.SocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class service {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserRepository userRepository;

    //-------------------------------Album----------------------------------------------
    public List<Album> getAllAlbum() {
        List<Album> documents = (List<Album>) albumRepository.findAll();
        if (documents.size() > 0)
            return documents;
        else
            return new ArrayList<Album>();
    }

    public Album getAlbum(int id) {
        Optional<Album> resource = albumRepository.findById(id);
        if (resource.isPresent()) {
            return resource.get();
        } else {
            return null;
        }
    }

    public Album getAlbumByName(String name) {
        Optional<Album> album = Optional.ofNullable(albumRepository.findByName(name));
        if (album.isPresent())
            return album.get();
        else
            return null;
    }

    public ResponseEntity addAlbum(Album resource) {
        if (resource.getAlbumID() == null) {
            System.out.println(resource);
            albumRepository.save(resource);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public boolean deleteAlbum(Integer id) {
        Optional<Album> resource = albumRepository.findById(id);
        if (resource.isPresent()) {
            albumRepository.deleteById(id);
            return true;
        } else
            return false;
    }

    public void updateAlbum(Album album) {
        albumRepository.save(album);
    }

    //-------------------------------artist----------------------------------------------
    public List<Artist> getAllArtist() {
        List<Artist> artists = (List<Artist>) artistRepository.findAll();
        if (artists.size() > 0)
            return artists;
        else
            return new ArrayList<>();
    }

    public Artist getArtist(int id) {
        Optional<Artist> resource = artistRepository.findById(id);
        if (resource.isPresent()) {
            return resource.get();
        } else {
            return null;
        }
    }

    public ResponseEntity addArtist(Artist resource) {
        if (resource.getArtistID() == null) {
            System.out.println(resource);
            artistRepository.save(resource);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public boolean deleteArtist(Integer id) {
        Optional<Artist> resource = artistRepository.findById(id);
        if (resource.isPresent()) {
            artistRepository.deleteById(id);
            return true;
        } else
            return false;

    }

    public boolean updateArtist(Artist artist) {
        artistRepository.save(artist);
        return true;
    }

    //-------------------------------Media----------------------------------------------
    public List<Media> getAllMedia() {
        List<Media> documents = (List<Media>) mediaRepository.findAll();
        if (documents.size() > 0)
            return documents;
        else
            return new ArrayList<Media>();
    }

    public Media getMedia(int id) {
        Optional<Media> resource = mediaRepository.findById(id);
        if (resource.isPresent()) {
            return resource.get();
        } else {
            return null;
        }
    }

    public void addMedia(Media media) {
//        if (media.getMediaID() == null) {
//            System.out.println(media);
//            mediaRepository.save(media);
//            return new ResponseEntity(HttpStatus.OK);
//        } else {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
        if (media != null)
            mediaRepository.save(media);
    }

    public boolean deleteMedia(int id) {
        Optional<Media> resource = mediaRepository.findById(id);
        if (resource.isPresent()) {
            mediaRepository.deleteById(id);
            return true;
        } else
            return false;

    }

    public void updateMedia(Media media) {
        if (media.getMediaID() == null)
            mediaRepository.save(media);
        else {
            Optional<Media> existingMedia = mediaRepository.findById(media.getMediaID());
            if (existingMedia.isPresent()) {
                Media newMedia = existingMedia.get();
                newMedia.setName(media.getName());
                newMedia.setGenre(media.getGenre());
                newMedia.setPublishDate(media.getPublishDate());
                newMedia.setLength(media.getLength());
                newMedia.setScore(media.getScore());
                mediaRepository.save(newMedia);
            }
        }
    }

    //-------------------------------artist----------------------------------------------
    public List<User> getAllUser() {
        List<User> documents = (List<User>) userRepository.findAll();
        if (documents.size() > 0)
            return documents;
        else
            return new ArrayList<User>();
    }

    public User getUser(int id) {
        Optional<User> resource = userRepository.findById(id);
        if (resource.isPresent()) {
            return resource.get();
        } else {
            return null;
        }
    }


    public ResponseEntity addUser(User resource) {
        if (resource.getUserID() == null) {
            System.out.println(resource);
            userRepository.save(resource);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public boolean deleteUser(int id) {
        Optional<User> resource = userRepository.findById(id);
        if (resource.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else
            return false;
    }

    public void updateUser(User user) {
        if (user.getUserID() == null)
            userRepository.save(user);
        else {
            Optional<User> existingUser = userRepository.findById(user.getUserID());
            if (existingUser.isPresent()) {
                User newUser = existingUser.get();
                newUser.setFirstname(user.getFirstname());
                newUser.setLastname(user.getLastname());
                newUser.setPassword(user.getPassword());
                userRepository.save(newUser);
            }
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent())
            return user.get();
        else
            return null;
    }

    public boolean findArtistNOTDuplicate(String firstname, String lastname, String birthdate) {
        List<Artist> resource = artistRepository.findByFirstnameAndLastnameAndBirthdate(firstname, lastname, birthdate);
        System.out.println(resource);
        if (resource.size() == 0) {
            return true;
        } else {
            return false;
        }
    }


}
