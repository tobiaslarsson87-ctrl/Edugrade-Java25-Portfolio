package se.edugrade.service;
import org.h2.engine.User;
import se.edugrade.dto.SimpleUsersDTO;
import se.edugrade.dto.UsersDTO;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.dto.subclasses.PatternMatcher;
import se.edugrade.entities.Post;
import se.edugrade.mappers.SimpleUsersMapper;
import se.edugrade.mappers.UsersMapper;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.UserNotFoundException;
import se.edugrade.repositories.UsersRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsersService {
    private final UsersRepository ur;

    public UsersService(UsersRepository ur) {
        this.ur = ur;
    }

    public List<UsersDTO> showAll(){
        return ur.findAll().stream()
                .map(UsersMapper::DTOMapper)
                .toList();
    }

    public List<SimpleUsersDTO> listAll(){
        return ur.findAll().stream()
                .map(SimpleUsersMapper::simpleMap)
                .toList();
    }

    //BETYGSKRITERIUM: Visa Posts av personer du följer
    public List<ContentType> friendPosts(Users currentUser){
        //Hämta IDs på alla posts gjorda av följare
        List<Long> ids = ur.findPostsByFollowers(currentUser.getId());
        //Hämta alla Post Objekt baserat på IDs
        List<Post> posts = ur.findFriendPostsById(ids);
        //Map om till rätt Post Type och returnera
        return posts.stream()
                .map(PatternMatcher::unknownType)
                .toList();
    }

    //VISA post från currentUser
    public List<ContentType> browsingPosts(Users currentUser){
        List<Post> posts = ur.currentUserPosts(currentUser.getId());
        return posts.stream()
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public UsersDTO findById(Long id){
        return ur.findById(id)
                .map(UsersMapper::DTOMapper)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " doesn't exist."));
    }

    public List<UsersDTO> findByName(String key){
        if(key == null){
            throw new IllegalArgumentException("Search word can not be empty.");
        }
        String searchWord = key.trim().toLowerCase();
        return ur.findAll().stream()
                .filter(u -> Optional.ofNullable(u.getUserName())
                .map(String::trim)
                .map(String::toLowerCase)
                .map(n -> n.contains(searchWord))
                .orElse(false))
                .map(UsersMapper::DTOMapper)
                .toList();
    }

    //Comparator tappade referensen till Users vid användning av reverse() så var tvungen att definieras explicit på comparator
    public List<UsersDTO> sortByFollowers(){
        return ur.findAll().stream()
                .sorted(Comparator.<Users>comparingInt(u -> u.getFollowers().size()).reversed())
                .map(UsersMapper::DTOMapper)
                .toList();
    }

    public List<UsersDTO> sortByPosts(){
        return ur.findAll().stream()
                .sorted(Comparator.<Users>comparingInt(u -> u.getPosts().size()).reversed())
                .map(UsersMapper::DTOMapper)
                .toList();
    }

    public List<UsersDTO> sortByNewest(){
        return ur.findAll().stream()
                .sorted(Comparator.comparing(Users::getCreatedAt).reversed())
                .map(UsersMapper::DTOMapper)
                .collect(Collectors.toList());
    }

    public List<UsersDTO> sortByOldest(){
        return ur.findAll().stream()
                .sorted(Comparator.comparing(Users::getCreatedAt))
                .map(UsersMapper::DTOMapper)
                .collect(Collectors.toList());
    }

    public void followService(Long followerID, Long followThisID){
        ur.followTransaction(followerID, followThisID);
    }

    public void unFollowService(Long unfollowerID, Long unfollowedID){
        ur.unfollowTransaction(unfollowerID, unfollowedID);
    }
}
