package SpringBoot.SocialMedia.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer userID;
    private String firstname;
    private String lastname;
    private String picture;
    private String biography;
    private String username;
    private String password;
    private String email;

    @ManyToMany
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "mediaID"))
    private List<Media> likeMedia =new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "views", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "mediaID"))
    private List<Media> viewMedia =new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "follows", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "artistID"))
    private List<Artist> followArtist =new ArrayList<>();

    public User() {

    }

    public User(String firstname, String lastname, String picture, String biography, String username, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.picture = picture;
        this.biography = biography;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Media> getLikeMedia() {
        return likeMedia;
    }

    public void setLikeMedia(List<Media> likeMedia) {
        this.likeMedia = likeMedia;
    }

    public List<Media> getViewMedia() {
        return viewMedia;
    }

    public void setViewMedia(List<Media> viewMedia) {
        this.viewMedia = viewMedia;
    }

    public List<Artist> getFollowArtist() {
        return followArtist;
    }

    public void setFollowArtist(List<Artist> followArtist) {
        this.followArtist = followArtist;
    }

    @Override
    public String toString() {
        return "user{" +
                "userID=" + userID +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", picture='" + picture + '\'' +
                ", biography='" + biography + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
