package SpringBoot.SocialMedia.controller;

import SpringBoot.SocialMedia.model.Album;
import SpringBoot.SocialMedia.model.Artist;
import SpringBoot.SocialMedia.model.Media;
import SpringBoot.SocialMedia.model.User;
import SpringBoot.SocialMedia.service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class contrller {
    @Autowired
    private service service;

    //---------------------------index-----------------------------------------------------
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/artist")
    public String artist() {
        return "artist";
    }

//    @GetMapping("/artists")
//    public String artists() {
//        return "artists";
//    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/playlist")
    public String playlist() {
        return "playlist";
    }

    @GetMapping("/category")
    public String category() {
        return "category";
    }


    //------------------------------album-------------------------------------------------------------------
    @RequestMapping("/album/add")
    public String addAlbum() {
        Album n = new Album("name", "time", 1, "gen");
        service.addAlbum(n);
        System.out.println("saved in Album!");
        return "index";
    }

    @RequestMapping("/album/delete")
    public String deleteAlbum() {
        service.deleteAlbum(1);
        System.out.println("delete Album!");
        return "index";
    }

    @RequestMapping("album/update/{id}")
    public ResponseEntity updateAlbum(@PathVariable("id") int id, @RequestParam("albumName") String albumName) {
        System.out.println("update album!!!");
        Album album = service.getAlbum(id);
        if (album != null) {
            album.setName(albumName);
            service.updateAlbum(album);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/album/getAll")
    public String getAllAlbum() {
        System.out.println(service.getAllAlbum());
        System.out.println("get All Album!");
        return "index";
    }

    //------------------------------artist-------------------------------------------------------------------
    @RequestMapping("/artist/add")
    public String addarist() {
        Artist n = new Artist("Jennifer", "Brown", "img/playlist/1.jpg", "bio", "bi");
        service.addArtist(n);
        System.out.println("saved in artist!");
        return "index";
    }

    @RequestMapping("/artist/delete")
    public String deleteArtist() {
        service.deleteArtist(1);
        System.out.println("delete Album!");
        return "index";
    }

    @RequestMapping("artist/update/{id}")
    public ResponseEntity updateArtist(@PathVariable("id") int id, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                       @RequestParam("bio")
                                               String bio,
                                       @RequestParam("picture")
                                               String picture) {
        System.out.println("update artist!!!");
        Artist artist = service.getArtist(id);
        if (artist != null) {
            artist.setFirstname(firstName);
            artist.setLastname(lastName);
            artist.setBiography(bio);
            artist.setPicture(picture);
            service.updateArtist(artist);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/artists")
    public @ResponseBody ModelAndView getAllArtists() {
        System.out.println("get All artists!");
        ModelAndView model = new ModelAndView();
        List<Artist> artists = service.getAllArtist();
        System.out.println(artists.get(0));
        model.addObject("artists", artists);
        model.setViewName("artists");
        return model;
    }

    @GetMapping(path = "/artist/{id}")
    public ModelAndView getArtist(@PathVariable("id") int id) {
        ModelAndView model = new ModelAndView();
        System.out.println(id);
        Album album = service.getAlbum(id);
        System.out.println(album.toString());

        model.addObject("album", album);
        model.setViewName("artist");

        return model;
    }

    //------------------------------media-------------------------------------------------------------------
    @RequestMapping("/media/add")
    public String addmedia() {
        Media n = new Media("n", 1, "gen", 100, "time");
        service.addMedia(n);
        System.out.println("saved in Album!");
        return "index";
    }

    @RequestMapping("/media/delete")
    public String deletemedia() {
        service.deleteMedia(1);
        System.out.println("delete Album!");
        return "index";
    }

    @RequestMapping("media/update/{id}")
    public ResponseEntity updateMedia(@PathVariable("id") int id, @RequestParam("name") String name) {
        System.out.println("update media!!!");
        Media media = service.getMedia(id);
        if (media != null) {
            media.setName(name);
            service.updateMedia(media);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/media/getAll")
    public String getAllmedia() {
        System.out.println(service.getAllMedia());
        System.out.println("get All Album!");
        return "index";
    }

    //------------------------------user-------------------------------------------------------------------
    @RequestMapping("/user/add")
    public String adduser() {
        User n = new User("fn", "ln", "pic", "bio", "us", "pass", "email");
        service.addUser(n);
        System.out.println("saved in Album!");
        return "index";
    }

    @RequestMapping("/user/delete")
    public String deleteuser() {
        service.deleteUser(1);
        System.out.println("delete Album!");
        return "index";
    }

    @RequestMapping("user/update/{id}")
    public ResponseEntity updateUser(@PathVariable("id") int id, @RequestParam("password") String password) {
        System.out.println("update user!!!");
        User user = service.getUser(id);
        if (user != null) {
            user.setPassword(password);
            service.updateUser(user);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/user/getAll")
    public String getAlluser() {
        System.out.println(service.getAllUser());
        System.out.println("get All Album!");
        return "index";
    }

    //------------------------------join table-------------------------------------------------------------------
    @RequestMapping("/user/like")
    public String like() {
        User u = new User("l", "l", "l", "l", "l", "l", "l");
        Media m = new Media("l", 1, "l", 100, "l");
        service.addMedia(m);
        System.out.println("saved in Album!");
        List like = u.getLikeMedia();
        like.add(m);
        u.setLikeMedia(like);
        System.out.println("add in list");
        service.addUser(u);
        System.out.println("saved in Album!");
        return "index";
    }

    @RequestMapping("/user/view")
    public String view() {
        User u = new User("v", "v", "v", "v", "v", "v", "v");
        Media m = new Media("v", 2, "v", 100, "v");
        service.addMedia(m);
        System.out.println("saved !");
        List view = u.getViewMedia();
        view.add(m);
        u.setViewMedia(view);
        System.out.println("add in list");
        service.addUser(u);
        System.out.println("saved !");
        return "index";
    }

    @RequestMapping("/user/follow")
    public String follow() {
        User u = new User("f", "f", "f", "f", "f", "f", "f");
        Artist n = new Artist("f", "f", "f", "f", "f");
        service.addArtist(n);
        System.out.println("saved ");
        List follow = u.getFollowArtist();
        follow.add(n);
        u.setFollowArtist(follow);
        System.out.println("add in list");
        service.addUser(u);
        System.out.println("saved ");
        return "index";
    }

    @RequestMapping("/artist/compile")
    public String compile() {
        Artist u = new Artist("c", "c", "c", "c", "c");
        Album n = new Album("c", "c", 1, "c");
        service.addAlbum(n);
        System.out.println("saved ");
        List compile = u.getCompileAlbum();
        compile.add(n);
        u.setCompileAlbum(compile);
        System.out.println("add in list");
        service.addArtist(u);
        System.out.println("saved ");
        return "index";
    }

    @RequestMapping("/album/contain")
    public String contain() {
        Album u = new Album("c", "c", 1, "c");
//        service.addAlbum(u);
        Media n = new Media("v", 2, "v", 100, "v");
        List med = new ArrayList();
        med.add(n);
        service.connect(med, u);

        System.out.println("saved ");
        return "index";
    }
}
