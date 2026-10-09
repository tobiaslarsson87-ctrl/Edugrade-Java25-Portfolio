package se.edugrade.menu;

import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.CommentDTO;
import se.edugrade.dto.HashtagDTO;
import se.edugrade.dto.LikeDTO;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.dto.subclasses.PatternMatcher;
import se.edugrade.entities.*;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.repositories.CommentRepository;
import se.edugrade.repositories.HashtagRepository;
import se.edugrade.repositories.LikeRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.service.CommentService;
import se.edugrade.service.HashtagService;
import se.edugrade.service.LikeService;
import se.edugrade.service.PostService;
import se.edugrade.utility.UserInput;
import se.edugrade.utility.showFeed;
import java.util.*;

// DOROTEAS POST MENU copy/paste från annan branch.
//Dorotea start
public class PostMenu {

    public static void run(EntityManagerFactory emf, Users currentUser) {
        boolean loop = true;
        while (loop) {
            System.out.println("---- 📝 POST MENU 📝 ----");
            System.out.println("1. ➕Create post");
            System.out.println("2. 💬️Comment post");
            System.out.println("3. ❤️Like post");
            System.out.println("4. 💔Unlike post");
            System.out.println("5. ✏️Search posts");
            System.out.println("6  🔄Updated post");
            System.out.println("7. ➖Deleted post ");
            System.out.println("8. #️⃣Show trending hashtags");
            System.out.println("9. 🏙️Show feed");
            System.out.println("0. 🔙Back to main menu");

            switch (UserInput.getInt()) {
                case 1 -> createPost(emf, currentUser);
                case 2 -> commentPost(emf, currentUser);
                case 3 -> likePost(emf, currentUser);
                case 4 -> unlikePost(emf, currentUser);
                case 5 -> searchPost(emf);
                case 6 -> updatePost(emf, currentUser);
                case 7 -> deletePost(emf);
                case 8 -> showTrendingHashtags(emf);
                case 9 -> showFeedMenu(emf);
                case 0 -> loop = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // =========================
    // 1. CREATE POST
    // =========================

    private static void createPost(EntityManagerFactory emf, Users currentUser) {

        System.out.println(" === Create a post ===");
        System.out.println("What post do you want to create? ");
            showFeed.showCreatePostMenu();   // UI flyttad till showFeed

                switch (UserInput.getInt()) {
                    case 1 -> text(emf, currentUser);
                    case 2 -> image(emf, currentUser);
                    case 3 -> video(emf, currentUser);
                    case 4 -> link(emf, currentUser);
                    case 0 -> PostMenu.run(emf,currentUser); //Går tillbaka till PostMenu
                    default -> System.out.println("Please choose 1-4");

                }
        }
        // Skapar en TextPost
    private static void text(EntityManagerFactory emf, Users currentUser){
        PostRepository pr = new PostRepository(emf);
        showFeed.showTextHeader();
        String text = UserInput.getString();
        TextPost tp = new TextPost(currentUser,text);
        Set<Hashtag> hashtags = addHashtag(emf);
        tp.setHashtags(hashtags); // Lägger till Hashtagen man skapat till Posten
        pr.create(tp); // Skickar TextPosten man skapat till DB
        ContentType dto = PatternMatcher.unknownType(tp); // Gör om entity till DTO
        System.out.println("✅ Created Post\n" + dto); // Skriver ut TextPost via TextPost dto
    }
    // Skapar en ImagePost
    private static void image(EntityManagerFactory emf, Users currentUser) {
        PostRepository pr = new PostRepository(emf);
        showFeed.showImageHeader();
        String caption = UserInput.getString();
        String url = UserInput.getUrl();
        ImagePost ip = new ImagePost(currentUser, url, caption);
        Set<Hashtag> hashtags = addHashtag(emf);
        ip.setHashtags(hashtags);
        pr.create(ip);
        ContentType dto = PatternMatcher.unknownType(ip);
        System.out.println("✅Created Post\n " + dto);
    }
    // Skapar en VideoPos
    private static void video(EntityManagerFactory emf, Users currentUser) {
        PostRepository pr = new PostRepository(emf);
        showFeed.showVideoHeader();
        String url = UserInput.getUrl();
        System.out.println("⏳ How long is the video?");
        int sec = UserInput.getInt();
        Set<Hashtag> hashtags = addHashtag(emf);
        VideoPost vp = new VideoPost(currentUser,url, sec);
        vp.setHashtags(hashtags);
        pr.create(vp);
        ContentType dto = PatternMatcher.unknownType(vp);
        System.out.println("✅ Created post\n" + dto);
    }
    // Skapar en linkPost
    private static void link(EntityManagerFactory emf, Users currentUser) {
        PostRepository pr = new PostRepository(emf);
        showFeed.showLinkHeader();
        String url = UserInput.getUrl();
        System.out.println("✏️What does it describe?");
        String description = UserInput.getString();
        LinkPost lp = new LinkPost(currentUser,url,description);
        Set<Hashtag> hashtags = addHashtag(emf);
        lp.setHashtags(hashtags);
        pr.create(lp);
        ContentType dto = PatternMatcher.unknownType(lp);
        System.out.println("✅Created post\n " + dto);
    }
    // Används i alla post för att fråga om man vill skapa hashtag
    private static Set<Hashtag> addHashtag(EntityManagerFactory emf) {
        Set<Hashtag> hashtags = new HashSet<>();
        System.out.println("#️⃣Do you want to add Hashtag? ");
        System.out.println("[1] ✅Yes");
        System.out.println("[2] ❌No");

        switch (UserInput.getInt()) {
            case 1 -> {
               return setHastag(emf); // Skickar den hashtagen man skapar till db
            }
            case 2 -> {
                return new HashSet<>(); // Gör att det inte blir någon Hashtag på den bilen man skapar
            }
        }
        return new HashSet<>();
    }
    // Skapar hashtagen
    private static Set<Hashtag> setHastag(EntityManagerFactory emf){
        HashtagRepository hr = new HashtagRepository(emf);
        Set<Hashtag> hashtags = new HashSet<>();
        while (true){
            System.out.println("✏️Write your hashtag? (type 'done' to finish) ");
            String tag = UserInput.getString();
            if(tag.equals("DONE".toLowerCase())) break;


            Hashtag exist = hr.findByTag(tag);
            if (exist == null){
            Hashtag hashtag = new Hashtag(tag);
            hr.create(hashtag);
            exist = hr.findByTag(tag);
        }
            hashtags.add(exist);

    }     return hashtags;
    }
    // Menu för att updatePost
    private static void updatePost(EntityManagerFactory emf,Users currentUser){
        System.out.println("--- 🔄Update a post---");
        System.out.println("🏙️What post do you want to update?");
        showFeed.printSchemaForCreate();

        int choose = UserInput.getInt();
        switch (choose){
            case 1 -> updateText(emf);
            case 2 -> updateImage(emf);
            case 3 -> updateVideo(emf);
            case 4 -> updateLink(emf);
            case 0 -> PostMenu.run(emf,currentUser);
        }

    }
    // Updaterar en TextPost
    private static void updateText(EntityManagerFactory emf) {
        PostRepository pr = new PostRepository(emf);
        PostService ps = new PostService(pr);
        System.out.println(ps.showAllText());

        System.out.println("📝What TextPost do you want to update? [ID] ");
        Long id = UserInput.getLong();

        Optional<Post> optionalPost = pr.findById(id);

        if(optionalPost.isEmpty()){
            System.out.println("⚠️Post with ID + " + id + " not found!");
            return;
        }
        Post post = optionalPost.get();
        if (!(post instanceof TextPost tp)){
            System.out.println("❌This post is not a TextPost");
            return;
        }
        System.out.println("✏️Write the updated text: ");
        tp.setText(UserInput.getString());
        pr.update(tp);
        System.out.println("✅Post Updated: " + id);

    }
    // Updaterar en ImagePost
    private static void updateImage(EntityManagerFactory emf){
        PostRepository pr = new PostRepository(emf);
        PostService ps = new PostService(pr);
        System.out.println(ps.showAllImage());

        System.out.println("🏙️What ImagePost do you want to update? [ID] ");
        Long id = UserInput.getLong();

        Optional<Post> optionalPost = pr.findById(id);

        if(optionalPost.isEmpty()){
            System.out.println("⚠️Post with ID + " + id + " not found!");
            return;
        }
        Post post = optionalPost.get();
        if (!(post instanceof ImagePost ip)){
            System.out.println("❌This post is not a ImagePost");
            return;
        }
        System.out.println("🔗Paste in the new Url:");
        ip.setImageUrl(UserInput.getUrl());
        System.out.println("✏️Write the updated caption: ");
        ip.setCaption(UserInput.getString());
        pr.update(ip);
        System.out.println("✅Post Updated: " + id);

    }
    // Updaterar en VideoPost
    private static void updateVideo(EntityManagerFactory emf){
        PostRepository pr = new PostRepository(emf);
        PostService ps = new PostService(pr);
        System.out.println(ps.showAllVideo());

        System.out.println("🎥What VideoPost do you want to update? [ID] ");
        Long id = UserInput.getLong();

        Optional<Post> optionalPost = pr.findById(id);

        if(optionalPost.isEmpty()){
            System.out.println("⚠️Post with ID + " + id + " not found!");
            return;
        }
        Post post = optionalPost.get();
        if (!(post instanceof VideoPost vp)){
            System.out.println("❌This post is not a VideoPost");
            return;
        }
        System.out.println("🔗Paste in the new Url:");
        vp.setVideoUrl(UserInput.getUrl());
        System.out.println("⏳How long is the new video?");
        vp.setDurationSeconds(UserInput.getInt());
        pr.update(vp);
        System.out.println("✅Post Updated: " + id);

    }
    // Updaterar en LinkPost
    private static void updateLink(EntityManagerFactory emf){
        PostRepository pr = new PostRepository(emf);
        PostService ps = new PostService(pr);
        System.out.println(ps.showAllLink());

        System.out.println("🔗What LinkPost do you want to update? [ID] ");
        Long id = UserInput.getLong();

        Optional<Post> optionalPost = pr.findById(id);

        if(optionalPost.isEmpty()){
            System.out.println("⚠️Post with ID + " + id + " not found!");
            return;
        }
        Post post = optionalPost.get();
        if (!(post instanceof LinkPost lp)){
            System.out.println("❌This post is not a LinkPost");
            return;
        }
        System.out.println("🔗Paste in the new Url:");
        lp.setLinkUrl(UserInput.getUrl());
        System.out.println("✏️Write the updated description: ");
        lp.setDescription(UserInput.getString());
        pr.update(lp);
        System.out.println("✅Post Updated: " + id);

    }
    // Deletar en post
    private static void deletePost(EntityManagerFactory emf){
        PostRepository pr = new PostRepository(emf);
        PostService ps = new PostService(pr);

        System.out.println(ps.showAll()); // Visar alla post

        System.out.println("🆔 What post do you want to delete? [ID]: ");

        Long id = UserInput.getLong();

        System.out.println("🏙️The post you choose: " + id);

        System.out.println("⚠️Are you sure you want to delete this post? ");
        System.out.println("[1] ✅Yes");
        System.out.println("[2] ❌No");

        int number = UserInput.getInt();

        if (number == 1){ // Deletar den post man valt
            pr.delete(id);
            System.out.println("✅Deleted post: " + id);
        } else if (number == 2) { // Gör inget med posten man valt
            System.out.println("❌The post is not deleted: " + id);
        }


    }



    //Dorotea slut

//Cathy start meny

    //----------------------------------------------------------
// 2. COMMENT
// ----------------------------------------------------------
    private static void commentPost(EntityManagerFactory emf, Users currentUser) {

        // Repositories & service (DB-access + logik)
        CommentService commentService = new CommentService(emf);

        // Visa kommenterbara inlägg + prompt
        showFeed.showCommentSelection(emf);

        // Användaren väljer post att kommentera
        Optional<Post> postOpt = validatePostSelection(emf, currentUser);
        if (postOpt.isEmpty()) return;

        // Användaren skriver kommentar
        showFeed.showWriteCommentText();
        String text = UserInput.getString();

        // Spara kommentar i DB via service (transaction + DTO)
        CommentDTO dto = commentService.createComment(postOpt.get(), currentUser, text);

        // Bekräftelse till användaren
        showFeed.showCommentSuccess(currentUser.getUserName(), dto.postId(), dto.text());
    }


    //----------------------------------------------------------
// 3. LIKE
//----------------------------------------------------------
    private static void likePost(EntityManagerFactory emf, Users currentUser) {

        // Repository + Service (hanterar logik och rollback)
        LikeService likeService = new LikeService(new LikeRepository(emf));

        // Visa likebara inlägg + prompt
        showFeed.showLikeSelection(emf, currentUser);

        Optional<Post> postOpt = validatePostSelection(emf, currentUser);
        if (postOpt.isEmpty()) return;
        long postId = postOpt.get().getId();


        // 🔍 Kontrollera via SERVICE om användaren redan likeat
        if (likeService.hasUserLikedPost(currentUser.getId(), postId)) {
            showFeed.showLikeAlreadyExists(currentUser.getUserName(), postId);
            return;
        }

        //Försöker gilla posten
        try {
            LikeDTO dto = likeService.likePost(postOpt.get(), currentUser);   // <-- använder returvärdet
            System.out.println(dto);                                          // valfri debug/visning
            showFeed.showLikeSuccess(currentUser.getUserName(), postId);
        } catch (InvalidContentException ex) {
            showFeed.showLikeAlreadyExists(currentUser.getUserName(), postId);
        }
    }

    //----------------------------------------------------------
// 4. UNLIKE
//----------------------------------------------------------
    private static void unlikePost(EntityManagerFactory emf, Users currentUser) {

        LikeService likeService = new LikeService(new LikeRepository(emf));

        // UI – visa vilka inlägg som kan väljas
        showFeed.showUnlikeSelection(emf);

        Optional<Post> postOpt = validatePostSelection(emf, currentUser);
        if (postOpt.isEmpty()) return;

        long postId = postOpt.get().getId();

        try {
            likeService.unlikePost(postOpt.get(), currentUser);
            showFeed.showUnlikeSuccess(currentUser.getUserName(), postId);

        } catch (InvalidContentException ex) {
            showFeed.showUnlikeNotFound(currentUser.getUserName(), postId);
        }
    }


    //----------------------------------------------------------
// 4. SEARCH
//----------------------------------------------------------
    private static void searchPost(EntityManagerFactory emf) {

        // Service för postsökning (DTO-baserad)
        PostService service = new PostService(new PostRepository(emf));

        showFeed.showSearchHeader();
        showFeed.showSearchPrompt();

        // Hämta sökord
        String keyword = UserInput.getString();

        // Kör sökning
        List<ContentType> results = service.search(keyword);

        // Hantera tomt resultat
        if (results.isEmpty()) {
            showFeed.showSearchNoResults();
            return;
        }

        // Visa resultat
        showFeed.showSearchResults(results);
    }


    //----------------------------------------------------------
// 5. TRENDING
//----------------------------------------------------------
    private static void showTrendingHashtags(EntityManagerFactory emf) {

        HashtagRepository repo = new HashtagRepository(emf);   // Hämtar hashtags från DB
        HashtagService service = new HashtagService();         // Sortering + topplista-logik

        List<Hashtag> hashtags = repo.findAll();
        if (hashtags.isEmpty()) { showFeed.showNoHashtagsFound(); return; }

        // Sortera och ta fram top 10 (LIST<String>)
        List<HashtagDTO> topList = service.topWeeklyGrowthHashtag(hashtags, 10);
        if (topList.isEmpty()) { showFeed.showNoTrendingHashtags(); return; }

        // Sorterar ut så bara tag skrivs ut
        List<String> topNames = topList.stream().map(HashtagDTO::tag).toList();

        // Skicka till UI
        showFeed.showTrendingResults(topNames);
    }


    //----------------------------------------------------------
// 6. FEED
//----------------------------------------------------------
    private static void showFeedMenu(EntityManagerFactory emf) {

        PostService postService = new PostService(new PostRepository(emf));
        LikeService likeService = new LikeService(new LikeRepository(emf));

        // Hämta data via service
        List<ContentType> posts = postService.sortByNewest();

        // Visa feed (UI sköts i showFeed)
        showFeed.showFeedHeader();
        showFeed.printPostsDTO(emf, posts);
    }


    // ----------------------------------------------------------
// GEMENSAM VALIDERING FÖR POST-AKTIONER (kommentar/like/unlike)
// ----------------------------------------------------------
    private static Optional<Post> validatePostSelection(EntityManagerFactory emf, Users currentUser) {

        PostRepository postRepo = new PostRepository(emf);

        //Läser in Post id som användaren skriver in och försöker hitta posten i databasen
        long postId = UserInput.getLong();
        Optional<Post> postOpt = postRepo.findById(postId);

        // Om posten inte finns → visa felmeddelande och avbryt
        if (postOpt.isEmpty()) {
            showFeed.showPostNotFound();
            return Optional.empty();
        }
        //Om användaren inte är inloggad → visa felmeddelande och avbryt
        if (currentUser == null) {
            showFeed.showNotLoggedIn();
            return Optional.empty();
        }

        return postOpt;
    }

//Cathy slut meny

}
