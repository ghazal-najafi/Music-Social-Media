package SpringBoot.SocialMedia.controller;

import SpringBoot.SocialMedia.model.*;
import SpringBoot.SocialMedia.service.service;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class SocialMediaController {
    private static final String BASE_DIR = "src/main/resources/static/music-files/";
    private static final String ALBUM_IMG_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private service service;

    //---------------------------index-----------------------------------------------------
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = null;
        Object userID = session.getAttribute("userID");
        if (userID != null) {
            System.out.println("kkk" + userID);
            User user = service.getUser((Integer) userID);
            userName = user.getUsername();
        }

        ModelAndView model = new ModelAndView();
        model.addObject("userName", userName);
        model.setViewName("index");
        return model;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/category")
    public String category() {
        return "category";
    }

    //------------------------------album-------------------------------------------------------------------
    @PostMapping("/album/add")
    public ResponseEntity addAlbum(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("genre") String genre) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        Path path = Paths.get(ALBUM_IMG_DIR + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        Album album = new Album(name, formatter.format(date), 0, genre, "static/uploads/" + file.getOriginalFilename());

        service.addAlbum(album);

        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @DeleteMapping("/album/delete/{id}")
    public ResponseEntity deleteAlbum(@PathVariable("id") int id) {
        boolean flag = service.deleteAlbum(id);
        if (flag)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("album/update/{id}")
    public ResponseEntity updateAlbum(@PathVariable("id") int id, @RequestParam("albumName") String albumName,
                                      @RequestParam("genre") String genre, @RequestParam("score") int score,
                                      @RequestParam("publishDate") String publishDate) {
        Album album = service.getAlbum(id);
        if (album != null) {
            album.setName(albumName);
            album.setGenre(genre);
            album.setScore(score);
            album.setPublishDate(publishDate);
            service.updateAlbum(album);
            return new ResponseEntity(album, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/album/{id}")
    public ModelAndView getAlbum(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        ModelAndView model = new ModelAndView();
        Album album = service.getAlbum(id);
        System.out.println(album.toString());

        List<Media> media = service.getMediaByAlbumID(id);
        System.out.println(media.toString());
        model.addObject("album", album);
        if (media.size() > 0)
            model.addObject("media", media.get(0));
        model.addObject("medias", media);
        model.addObject("userName", userName);
        model.setViewName("album");

        return model;
    }

    @GetMapping("/albums")
    public ModelAndView getAllAlbum(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        ModelAndView model = new ModelAndView();
        List<Album> albums = service.getAllAlbum();

        model.addObject("albums", albums);
        model.addObject("userName", userName);
        model.setViewName("albums");
        return model;
    }

    //------------------------------artist-------------------------------------------------------------------

    @PostMapping("/artist/add")
    public ResponseEntity AddArtist(@RequestParam("file") MultipartFile file,
                                    @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                    @RequestParam("biography") String biography, @RequestParam("birthDate") String birthDate) throws IOException {
        Path path = Paths.get(ALBUM_IMG_DIR + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        if (service.findArtistNOTDuplicate(firstName, lastName, birthDate)) {
            Artist artist = new Artist(firstName, lastName, "static/uploads/" + file.getOriginalFilename(), biography, birthDate);

            service.addArtist(artist);
            return new ResponseEntity<>(artist, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/artist/update/{id}")
    public ResponseEntity UpdateArtist(@PathVariable("id") int id, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                       @RequestParam("biography") String biography, @RequestParam("birthDate") String birthDate) {
        Artist artist = service.getArtist(id);
        artist.setFirstname(firstName);
        artist.setLastname(lastName);
        artist.setBiography(biography);
        artist.setBirthdate(birthDate);
        boolean response = service.updateArtist(artist);
        if (response)
            return new ResponseEntity<>(artist, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/artist/delete/{id}")
    public ResponseEntity deleteArtist(@PathVariable("id") int id) {
        boolean response = service.deleteArtist(id);
        if (response)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/artists")
    public @ResponseBody
    ModelAndView getAllArtists(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        ModelAndView model = new ModelAndView();
        List<Artist> artists = service.getAllArtist();

        model.addObject("artists", artists);
        model.addObject("userName", userName);
        model.setViewName("artists");
        return model;
    }

    @GetMapping(path = "/artist/{id}")
    public ModelAndView getArtist(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        ModelAndView model = new ModelAndView();
        Artist artist = service.getArtist(id);

        model.addObject("artist", artist);
        model.addObject("userName", userName);
        model.setViewName("artist");

        return model;
    }

    //------------------------------media-------------------------------------------------------------------

    @RequestMapping("/media/getAll")
    public ModelAndView getAllMedia(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");

        ModelAndView model = new ModelAndView();
        List<Media> medias = service.getAllMedia();

        model.addObject("medias", medias);
        model.addObject("userName", userName);
        model.setViewName("medias");
        return model;
    }

    @GetMapping("/media/{id}")
    public ModelAndView getMedia(HttpServletRequest request, @PathVariable int id) {
        HttpSession session = request.getSession();
        int viewCount = setView((Integer) session.getAttribute("userID"), id);

        ModelAndView model = new ModelAndView();
        Media media = service.getMedia(id);

        model.addObject("media", media);
        model.addObject("viewCount", viewCount);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("media");

        return model;
    }

    @GetMapping("/get/media/{id}")
    public ResponseEntity getMediaRest(@PathVariable int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int viewCount = setView((Integer) session.getAttribute("userID"), id);

        Media media = service.getMedia(id);
        System.out.println("media: " + media.toString());
        if (media == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .cacheControl(CacheControl.noCache().mustRevalidate())
                .body(media);
    }

    public int setView(int userID, int mediaID) {
        User user = service.getUser(userID);
        Media media = service.getMedia(mediaID);

        List view = user.getViewMedia();
//        view.add(media);
        user.setViewMedia(media);
        service.addOrUpdateUser(user);

        return view.size();
    }

    @GetMapping("/media/download/{id}")
    public ResponseEntity downloadMedia(@PathVariable int id) throws IOException {
        Media media = service.getMedia(id);
        String mediaName = media.getName();

        Path path = Paths.get(BASE_DIR + media.getPath());
        String mimeType = Files.probeContentType(path);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(mimeType))
//                .contentType(MediaType.parseMediaType("application/json"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaName + "\"")
                .contentLength(resource.contentLength())
                .cacheControl(CacheControl.noCache().mustRevalidate())
                .body(resource);
    }

    @PostMapping("/media/upload")
    public ResponseEntity addMedia(@RequestParam("file") MultipartFile file, @RequestParam("albumName") String albumName,
                                   @RequestParam("mediaName") String mediaName, @RequestParam("genre") String genre
            , @RequestParam("artistID") int artistID) {

        if (file.isEmpty())
            return (ResponseEntity) ResponseEntity.noContent();

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(BASE_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            Album album = service.getAlbumByName(albumName);
            if (album == null) {
                System.out.println("The required media is not exists !!");
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            Media uploadMedia = new Media();
            uploadMedia.setName(mediaName);
            uploadMedia.setAlbum(album);
            uploadMedia.setLength((int) file.getSize());
            uploadMedia.setGenre(genre);
            uploadMedia.setPath(file.getOriginalFilename());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            uploadMedia.setPublishDate(formatter.format(date));

            Artist artist = service.getArtist(artistID);
            System.out.println(artist.toString());
            if (artist != null)
                album.setCompiles(artist);


            service.addMedia(uploadMedia);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/media/delete/{id}")
    public ResponseEntity deleteMedia(@PathVariable("id") int id) {
        boolean response = service.deleteMedia(id);
        if (response)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/media/update/{id}")
    public ResponseEntity updateMedia(@PathVariable("id") int id, @RequestParam("score") int score,
                                      @RequestParam("name") String name, @RequestParam("genre") String genre,
                                      @RequestParam("length") int length, @RequestParam("publishDate") String publishDate) {
        Media media = service.getMedia(id);
        if (media != null) {
            media.setName(name);
            media.setGenre(genre);
            media.setLength(length);
            media.setPublishDate(publishDate);
            media.setScore(score);
            service.updateMedia(media);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    //------------------------------user-------------------------------------------------------------------

    @RequestMapping(value = "/user/signup")
    public ModelAndView registerUser(HttpServletRequest request, @RequestParam("username") String username,
                                     @RequestParam("email") String email, @RequestParam("password") String password) {
        ModelAndView model = new ModelAndView();
        User isExitUser = service.getUserByEmail(email);
        if (isExitUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setUsername(username);
            service.addOrUpdateUser(newUser);

            HttpSession session = request.getSession();
            session.setAttribute("userID", service.getUserByEmail(email).getUserID());
            session.setAttribute("userName", username);

            model.addObject("userName", username);
            model.setViewName("index");
        }
        model.setViewName("signup");
        return model;
    }

    @PostMapping(value = "/rest/user/signup")
    public ResponseEntity registerUserRest(HttpServletRequest request, @RequestParam("username") String username,
                                           @RequestParam("email") String email, @RequestParam("password") String password) {
        User isExitUser = service.getUserByEmail(email);
        if (isExitUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setUsername(username);
            service.addOrUpdateUser(newUser);

            HttpSession session = request.getSession();
            session.setAttribute("userID", service.getUserByEmail(email).getUserID());
            session.setAttribute("userName", username);

            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } else {
            System.out.println("User Already exists!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/login")
    public ModelAndView loginUser(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        User isExitUser = service.getUserByEmail(request.getParameter("email"));

        if (isExitUser != null && isExitUser.getPassword().equals(request.getParameter("password"))) {
            model.addObject("userName", isExitUser.getUsername());
            model.setViewName("index");
            return model;
        }
        model.setViewName("login");
        return model;
    }

    @PostMapping(value = "/rest/user/login")
    public ResponseEntity loginUserRest(HttpServletRequest request) {
        User isExitUser = service.getUserByEmail(request.getParameter("email"));
        if (isExitUser != null && isExitUser.getPassword().equals(request.getParameter("password"))) {
            HttpSession session = request.getSession();
            session.setAttribute("userID", isExitUser.getUserID());
            session.setAttribute("userName", isExitUser.getUsername());
            return new ResponseEntity<>(isExitUser, HttpStatus.OK);
        } else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));

        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.addObject("userName", user.getUsername());
        model.setViewName("profile");
        return model;
    }

    @GetMapping("/rest/profile")
    public ResponseEntity profileRest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/changePassword")
    public ModelAndView userEditPassword(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));

        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.addObject("userName", user.getUsername());
        model.setViewName("changePassword");
        return model;
    }

    @RequestMapping("/user/edit")
    public ModelAndView updateUser(@RequestParam("id") int id, @RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName, @RequestParam("biography") String biography) {
        ModelAndView model = new ModelAndView();
        User user = service.getUser(id);
        if (user != null) {
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setBiography(biography);
            service.addOrUpdateUser(user);
            model.addObject("userName", user.getUsername());
        }
        model.addObject("userName", null);
        model.setViewName("profile");
        return model;
    }

    @PutMapping("/rest/user/edit")
    public ResponseEntity updateUserRest(@RequestParam("id") int id, @RequestParam("firstName") String firstName,
                                         @RequestParam("lastName") String lastName, @RequestParam("biography") String biography) {
        User user = service.getUser(id);
        if (user != null) {
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setBiography(biography);
            service.addOrUpdateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/changePassword")
    public ModelAndView changePassword(HttpServletRequest request, @RequestParam("currentPassword") String currentPassword, @RequestParam("password") String password,
                                       @RequestParam("repeatNewPassword") String repeatNewPassword) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));
        System.out.println(user.toString());
        if (user != null && user.getPassword().equals(currentPassword) && password.equals(repeatNewPassword)) {
            user.setPassword(password);
            service.addOrUpdateUser(user);
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("changePassword");
        model.addObject("userName", user.getUsername());
        return model;
    }

    @PutMapping("/rest/changePassword")
    public ResponseEntity changePasswordRest(HttpServletRequest request, @RequestParam("currentPassword") String currentPassword, @RequestParam("password") String password,
                                             @RequestParam("repeatNewPassword") String repeatNewPassword) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));

        if (user != null && user.getPassword().equals(currentPassword) && password.equals(repeatNewPassword)) {
            user.setPassword(password);
            service.addOrUpdateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/getAll")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity<>(service.getAllUser(), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") int id) {
        boolean response = service.deleteUser(id);
        if (response)
            return new ResponseEntity<>("user deleted successfully!", HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    //------------------------------join table-------------------------------------------------------------------
    @RequestMapping("/like/{id}")
    public String like(HttpServletRequest request, @PathVariable("id") int id) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));

        Media media = service.getMedia(id);
        user.setLikeMedia(media);
        service.addOrUpdateUser(user);

        return "index";
    }

    @RequestMapping("/likedMedia")
    public ModelAndView getLikedMedia(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));
        List likes = user.getLikeMedia();
        int start = 0, end = likes.size();
        if (likes.size() > 10)
            start = likes.size() - 10;

        for (int i = start; i < end; i++) {
            Media media = (Media) likes.get(i);
            System.out.println(media.toString());
            Album album = media.getAlbum();
            System.out.println(album.toString());
            List<Artist> artist = album.getCompiles();
            System.out.println(artist.toString());
        }


//        List<Artist> artists = album.getCompiles

        ModelAndView model = new ModelAndView();
        model.addObject("likes", likes);
        model.setViewName("likedMedia");
        return model;
    }

    @RequestMapping("/view/{id}")
    public String userViews(@PathVariable("id") int id) {
        Media media = service.getMedia(id);
        List users = media.getViews();
        System.out.println(users.toString());
        return "index";
    }

    @RequestMapping(value = "/follow")
    public String follow(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));
        Artist artist = service.getArtist(5);
//        List<Artist> follow = user.getFollowArtist();
//        follow.add(artist);

        user.setFollowArtist(artist);
        service.addOrUpdateUser(user);
        System.out.println("saved ");

        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }

}

