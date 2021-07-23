package SpringBoot.SocialMedia.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumID;
    private String name;
    private String publishDate;
    private int score;
    private String genre;
    private String picture;

    @ManyToMany(mappedBy = "compileAlbum")
    private List<Artist> compiles = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Media> contain = new ArrayList<>();

    public Album() {

    }

    public Album(String name, String publishDate, int score, String genre, String picture) {
        this.name = name;
        this.publishDate = publishDate;
        this.score = score;
        this.genre = genre;
        this.picture = picture;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
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

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public List<Artist> getCompiles() {
        return compiles;
    }

    public void setCompiles(Artist artist) {
        if (!compiles.contains(artist))
            compiles.add(artist);
    }

    public List<Media> getContain() {
        return contain;
    }

    public void setContain(Media media) {
        if (!contain.contains(media))
            contain.add(media);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "album{" +
                "albumID=" + albumID +
                ", name='" + name + '\'' +
                ", publishdate=" + publishDate +
                ", score=" + score +
                ", genre='" + genre + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}






