package SpringBoot.SocialMedia.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mediaID;
    private String name;
    private int score;
    private String genre;
    private int lenght;
    private String publishdate;

    @ManyToMany(mappedBy = "likeMedia")
    private List<User> likes= new ArrayList<>();

    @ManyToMany(mappedBy = "viewMedia")
    private List<User> views= new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "albumID", nullable = true )
    private Album album = new Album();

    public Media() {

    }

    public Media(String name, int score, String genre, int lenght, String publishdate) {
        this.name = name;
        this.score = score;
        this.genre = genre;
        this.lenght = lenght;
        this.publishdate = publishdate;
    }

    public Integer getMediaID() {
        return mediaID;
    }

    public void setMediaID(Integer mediaID) {
        this.mediaID = mediaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public List<User> getViews() {
        return views;
    }

    public void setViews(List<User> views) {
        this.views = views;
    }

    public SpringBoot.SocialMedia.model.Album getAlbum() {
        return album;
    }

    public void setAlbum(SpringBoot.SocialMedia.model.Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "media{" +
                "albumID=" + mediaID +
                ", name='" + name + '\'' +
                ", publishdate='" + publishdate + '\'' +
                ", score=" + score +
                ", genre='" + genre + '\'' +
                ", lenght=" + lenght +
                '}';
    }
}
