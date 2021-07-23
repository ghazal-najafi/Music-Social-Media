package SpringBoot.SocialMedia.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistID;
    private String firstname;
    private String lastname;
    private String picture;
    private String biography;
    private String birthdate;

    @ManyToMany(mappedBy = "followArtist")
    private List<User> follows = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "compiles", joinColumns = @JoinColumn(name = "artistID"), inverseJoinColumns = @JoinColumn(name = "albumID"))
    private List<Album> compileAlbum = new ArrayList<>();

    public Artist() {

    }

    public Artist(String firstname, String lastname, String picture, String biography, String birthdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.picture = picture;
        this.biography = biography;
        this.birthdate = birthdate;
    }

    public Integer getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<User> getFollows() {
        return follows;
    }

    public void setFollowAlbum(User user) {
        if (!follows.contains(user))
            follows.add(user);
    }

    public List<Album> getCompileAlbum() {
        return compileAlbum;
    }

    public void setCompileAlbum(Album album) {
        if (!compileAlbum.contains(album))
            compileAlbum.add(album);
    }


    @Override
    public String toString() {
        return "artist{" +
                "artistID=" + artistID +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", picture='" + picture + '\'' +
                ", biography='" + biography + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
