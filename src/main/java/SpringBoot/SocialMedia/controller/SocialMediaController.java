package SpringBoot.SocialMedia.controller;

import SpringBoot.SocialMedia.model.*;
import SpringBoot.SocialMedia.service.service;
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
import java.util.*;


@Controller
public class SocialMediaController {
    private static final String BASE_DIR = "src/main/resources/static/music-files/";
    private static final String ALBUM_IMG_DIR = "src/main/resources/static/albums/";

    @Autowired
    private service service;

    //---------------------------index-----------------------------------------------------
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = null;
        Object userID = session.getAttribute("userID");
        if (userID != null) {
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
    public ResponseEntity addAlbum(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
                                   @RequestParam("genre") String genre) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        Path path = Paths.get(ALBUM_IMG_DIR + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        Album album = new Album(name, formatter.format(date), 0, genre, "albums/" + file.getOriginalFilename());
        service.addAlbum(album);

        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @DeleteMapping("/album/delete/{id}")
    public ResponseEntity deleteAlbum(@PathVariable("id") int id) {
        boolean flag = service.deleteAlbum(id);
        if (flag)
            return new ResponseEntity<>("album deleted successfully!", HttpStatus.OK);
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

    @GetMapping("/album/get/{id}")
    public ModelAndView getAlbum(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView model = new ModelAndView();
        Album album = service.getAlbum(id);

        List<Media> medias = service.getMediaByAlbumID(id);
        if (medias.size() > 0)
            model.addObject("media", medias.get(0));
        model.addObject("medias", medias);
        model.addObject("album", album);
        model.addObject("artists", album.getCompiles());
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("album");

        return model;
    }

    @GetMapping("/albums")
    public ModelAndView getAllAlbum(HttpServletRequest request) {
        HttpSession session = request.getSession();

        ModelAndView model = new ModelAndView();
        List<Album> albums = service.getAllAlbum();

        model.addObject("albums", albums);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("albums");
        return model;
    }

    @GetMapping("/rest/album/get/{id}")
    public ResponseEntity getAlbumRest(@PathVariable("id") int id) {
        Album album = service.getAlbum(id);
        return new ResponseEntity(album, HttpStatus.OK);
    }

    @GetMapping("/rest/albums")
    public ResponseEntity getAllAlbumRest() {
        return new ResponseEntity(service.getAllAlbum(), HttpStatus.OK);
    }

    //------------------------------artist-------------------------------------------------------------------

    @PostMapping("/artist/add")
    public ResponseEntity AddArtist(@RequestParam("file") MultipartFile file,
                                    @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                    @RequestParam("biography") String biography, @RequestParam("birthDate") String birthDate) throws IOException {
        Path path = Paths.get(ALBUM_IMG_DIR + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        if (service.findArtistNOTDuplicate(firstName, lastName, birthDate)) {
            Artist artist = new Artist(firstName, lastName, "artists/" + file.getOriginalFilename(), biography, birthDate);
            service.addArtist(artist);
            return new ResponseEntity<>(artist, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/artist/update/{id}")
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

    @DeleteMapping("/artist/delete/{id}")
    public ResponseEntity deleteArtist(@PathVariable("id") int id) {
        boolean response = service.deleteArtist(id);
        if (response)
            return new ResponseEntity<>("artist deleted successfully!", HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/rest/artists")
    public ResponseEntity getAllArtistsRest() {
        return new ResponseEntity<>(service.getAllArtist(), HttpStatus.OK);
    }


    @GetMapping(path = "/rest/artist/get/{id}")
    public ResponseEntity getArtistRest(@PathVariable("id") int id) {
        Artist artist = service.getArtist(id);
        if (artist != null)
            return new ResponseEntity<>(artist, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/artists")
    public @ResponseBody
    ModelAndView getAllArtists(HttpServletRequest request) {
        HttpSession session = request.getSession();

        ModelAndView model = new ModelAndView();
        List<Artist> artists = service.getAllArtist();

        model.addObject("artists", artists);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("artists");
        return model;
    }

    @GetMapping(path = "/artist/get/{id}")
    public ModelAndView getArtist(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();

        ModelAndView model = new ModelAndView();
        Artist artist = service.getArtist(id);
        List<Album> albums = artist.getCompileAlbum();

        model.addObject("artist", artist);
        model.addObject("albums", albums);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("artist");

        return model;
    }

    //------------------------------media-------------------------------------------------------------------

    @GetMapping("/medias")
    public ModelAndView getAllMedia(HttpServletRequest request) {
        Map<Media, Artist> topMedia = new HashMap();
        Map<Media, Artist> map = new HashMap();
        HttpSession session = request.getSession();

        ModelAndView model = new ModelAndView();
        List<Media> medias = service.getAllMedia();

        if (medias.size() > 3)
            for (int i = 0; i < 3; i++) {
                Media media = medias.remove(0);
                topMedia.put(media, media.getAlbum().getCompiles().get(0));
            }

        for (int i = 0; i < medias.size(); i++) {
            Media media = medias.get(i);
            topMedia.put(media, media.getAlbum().getCompiles().get(0));
        }
        model.addObject("topMedias", topMedia);
        model.addObject("medias", map);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("playlist");
        return model;
    }

    @GetMapping("/rest/media/getAll")
    public ResponseEntity getAllMediaRest() {
        return new ResponseEntity<>(service.getAllMedia(), HttpStatus.OK);
    }

    @GetMapping("/media/{id}")
    public ModelAndView getMedia(HttpServletRequest request, @PathVariable int id) {
        HttpSession session = request.getSession();
        long viewCount = 0;

        Media media = service.getMedia(id);
        if (session.getAttribute("userID") != null)
            viewCount = setView(service.getUser((Integer) session.getAttribute("userID")), media);
        System.out.println("view count: " + viewCount);

        ModelAndView model = new ModelAndView();
        model.addObject("media", media);
        model.addObject("artists", media.getAlbum().getCompiles());
        model.addObject("viewCount", viewCount);
        model.addObject("userName", session.getAttribute("userName"));
        model.setViewName("media");

        return model;
    }

    public int setView(User user, Media media) {
        user.setViewMedia(media);
        service.addOrUpdateUser(user);

        return user.getViewMedia().size();
    }

    @GetMapping("/rest/media/get/{id}")
    public ResponseEntity getMediaRest(@PathVariable int id, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Media media = service.getMedia(id);
        long viewCount = setView(service.getUser((Integer) session.getAttribute("userID")), media);
        System.out.println("view count: " + viewCount);

        if (media == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity(media, HttpStatus.OK);
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaName + "\"")
                .contentLength(resource.contentLength())
                .cacheControl(CacheControl.noCache().mustRevalidate())
                .body(resource);
    }

    @PostMapping("/media/upload")
    public ResponseEntity addMedia(@RequestParam("file") MultipartFile file, @RequestParam("albumName") String albumName,
                                   @RequestParam("mediaName") String mediaName, @RequestParam("genre") String genre) {
        Media uploadMedia = new Media();
        if (file.isEmpty())
            return (ResponseEntity) ResponseEntity.noContent();

        try {
            // Get the file and save it somewhere
            Path path = Paths.get(BASE_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            Album album = service.getAlbumByName(albumName);
            if (album == null) {
                System.out.println("The required media is not exists !!");
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            uploadMedia.setName(mediaName);
            uploadMedia.setAlbum(album);
            uploadMedia.setLength(convertToMinutes(file.getSize()));
            uploadMedia.setGenre(genre);
            uploadMedia.setPath(file.getOriginalFilename());
            System.out.println(uploadMedia);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            uploadMedia.setPublishDate(formatter.format(date));

            service.addOrUpdateMedia(uploadMedia);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String convertToMinutes(long length) {
        int mili = (int) (length / 1000);
        int sec = (mili / 1000) % 60;
        int min = (mili / 1000) / 60;
        return sec + ":" + min;
    }

    @RequestMapping(value = "/media/delete/{id}")
    public ResponseEntity deleteMedia(@PathVariable("id") int id) {
        boolean response = service.deleteMedia(id);
        if (response)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/media/update/{id}")
    public ResponseEntity updateMedia(@PathVariable("id") int id, @RequestParam("score") int score,
                                      @RequestParam("name") String name, @RequestParam("genre") String genre,
                                      @RequestParam("length") String length, @RequestParam("publishDate") String publishDate) {
        Media media = service.getMedia(id);
        if (media != null) {
            media.setName(name);
            media.setGenre(genre);
            media.setLength(length);
            media.setPublishDate(publishDate);
            media.setScore(score);
            service.addOrUpdateMedia(media);
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
        else
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
    public ModelAndView like(HttpServletRequest request, @PathVariable("id") int id) {
        ModelAndView model = new ModelAndView();

        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        if (userID != null) {
            User user = service.getUser(Integer.parseInt(userID));
            user.setLikeMedia(service.getMedia(id));
            service.addOrUpdateUser(user);

            model.addObject("userName", session.getAttribute("userName"));
            model.setViewName("likedMedia");
        } else
            model.setViewName("login");
        return model;
    }

    @PostMapping("/rest/like/{id}")
    public ResponseEntity likeRest(HttpServletRequest request, @PathVariable("id") int id) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));

        user.setLikeMedia(service.getMedia(id));
        service.addOrUpdateUser(user);
        return new ResponseEntity("true", HttpStatus.OK);

    }

    @GetMapping("/likedMedia")
    public ModelAndView getLikedMedia(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");

        if (userID != null) {
            User user = service.getUser(Integer.parseInt(userID));
            List<Media> medias = new ArrayList<>();
            List<Media> likes = user.getLikeMedia();
            int start = 0, end = likes.size();
            if (likes.size() > 10)
                start = likes.size() - 10;

            for (int i = start; i < end; i++)
                medias.add(likes.get(i));

            model.addObject("medias", medias);
            model.addObject("userName", user.getUsername());
            model.setViewName("likedMedia");
        } else model.setViewName("login");
        return model;
    }

    @GetMapping("/rest/likedMedia")
    public ResponseEntity getLikedMediaRest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");

        if (userID != null) {
            User user = service.getUser(Integer.parseInt(userID));
            List<Media> medias = new ArrayList<>();
            List<Media> likes = user.getLikeMedia();
            int start = 0, end = likes.size();
            if (likes.size() > 10)
                start = likes.size() - 10;

            for (int i = start; i < end; i++)
                medias.add(likes.get(i));
            return new ResponseEntity(medias, HttpStatus.OK);
        }
        return new ResponseEntity("you should login!", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @GetMapping("likes/getAll")
    public ResponseEntity getAllLikes() {
        return new ResponseEntity(service.getAllLikedMedia(), HttpStatus.OK);
    }

    @GetMapping("/rest/userLikes/{id}")
    public ResponseEntity getUserLikesRest(HttpServletRequest request, @PathVariable("id") int id) {
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");

        if (userID != null) {
            Media media = service.getMedia(id);

            List<User> users = new ArrayList<>();
            List<User> likes = media.getLikes();
            for (int i = 0; i < likes.size(); i++)
                users.add(likes.get(i));

            return new ResponseEntity(users, HttpStatus.OK);
        }
        return new ResponseEntity("you should login!", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    //    @RequestMapping("/view/{id}")
//    public String userViews(@PathVariable("id") int id) {
//        Media media = service.getMedia(id);
//        List users = media.getViews();
//        System.out.println(users.toString());
//        return "index";
//    }

    @RequestMapping("/follow/{id}")
    public String follow(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        if (userID == null)
            return "login";
        User user = service.getUser(Integer.parseInt(userID));
        Artist artist = service.getArtist(id);
        user.setFollowArtist(artist);
        service.addOrUpdateUser(user);

        return "index";
    }

    @PostMapping(value = "/rest/follow/{id}")
    public ResponseEntity followRest(@PathVariable("id") int id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = service.getUser((Integer) session.getAttribute("userID"));
        Artist artist = service.getArtist(id);
        user.setFollowArtist(artist);
        service.addOrUpdateUser(user);

        return new ResponseEntity("you followed artist successfully!", HttpStatus.OK);
    }


    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }

}

