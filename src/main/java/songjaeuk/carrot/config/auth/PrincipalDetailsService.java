package songjaeuk.carrot.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import songjaeuk.carrot.user.User;
import songjaeuk.carrot.user.UserDto;
import songjaeuk.carrot.user.UserRepository;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user =  userRepository.findById(username);

        if(user.isEmpty())
            return null;

        UserDto dto = new UserDto();
        dto.setId(user.get().getId());
        dto.setPassword(user.get().getPassword());
        dto.setRole(user.get().getRole());
        dto.setName(user.get().getName());

        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUser(dto);

        return principalDetails;

    }
}
