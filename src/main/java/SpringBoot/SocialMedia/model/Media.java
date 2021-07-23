package SpringBoot.SocialMedia.model;

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
    private int length;
    private String publishDate;

    @ManyToMany(mappedBy = "likeMedia")
    private List<User> likes = new ArrayList<>();

    @ManyToMany(mappedBy = "viewMedia")
    private List<User> views = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "albumID", nullable = true)
    private Album album = new Album();

    public Media() {

    }

    public Media(String name, int score, String genre, int length, String publishDate) {
        this.name = name;
        this.score = score;
        this.genre = genre;
        this.length = length;
        this.publishDate = publishDate;
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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(User user) {
        if (!likes.contains(user))
            likes.add(user);
    }

    public List<User> getViews() {
        return views;
    }

    public void setViews(User user) {
        if (!views.contains(user))
            views.add(user);
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
                ", publishdate='" + publishDate + '\'' +
                ", score=" + score +
                ", genre='" + genre + '\'' +
                ", lenght=" + length +
                '}';
    }
}
