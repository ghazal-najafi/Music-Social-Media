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

    public ResponseEntity addAlbum(Album resource) {
        if (resource.getAlbumID() == null) {
            System.out.println(resource);
            albumRepository.save(resource);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteAlbum(Integer id) {
        Optional<Album> resource = albumRepository.findById(id);
        if (resource.isPresent()) {
            albumRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public void updateAlbum(Album album) {
        if (album.getAlbumID() == null)
            albumRepository.save(album);
        else {
            Optional<Album> existingAlbum = albumRepository.findById(album.getAlbumID());
            if (existingAlbum.isPresent()) {
                Album newAlbum = existingAlbum.get();
                newAlbum.setName(album.getName());
                albumRepository.save(album);
            }
        }
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

    public ResponseEntity deleteArtist(Integer id) {
        Optional<Artist> resource = artistRepository.findById(id);
        if (resource.isPresent()) {
            artistRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public void updateArtist(Artist artist) {
        artistRepository.save(artist);
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

    public ResponseEntity addMedia(Media resource) {
        if (resource.getMediaID() == null) {
            System.out.println(resource);
            mediaRepository.save(resource);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity deleteMedia(Integer id) {
        Optional<Media> resource = mediaRepository.findById(id);
        if (resource.isPresent()) {
            mediaRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public void updateMedia(Media media) {
        mediaRepository.save(media);
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

    public ResponseEntity deleteUser(Integer id) {
        Optional<User> resource = userRepository.findById(id);
        if (resource.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
    public void connect(List<Media> med, Album alb) {
        if (alb != null && med != null) {
            alb.setContain(med);
            for (Media m : med)
                if (m != null){
                    m.setAlbum(alb);
                    addMedia(m);
                }
        }
    }

}
