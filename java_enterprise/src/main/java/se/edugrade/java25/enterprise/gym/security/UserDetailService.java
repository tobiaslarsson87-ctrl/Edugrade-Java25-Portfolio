package se.edugrade.java25.enterprise.gym.security;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.edugrade.java25.enterprise.gym.repository.UserRepository;
import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        se.edugrade.java25.enterprise.gym.model.User user = userRepository.findByUsername(username) //explicit instead of var for learning purpose
                .orElseThrow(() -> new UsernameNotFoundException("User by the name of: " + username + " was not found."));

                return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                );
    }
}
