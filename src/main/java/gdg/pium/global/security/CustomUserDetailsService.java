package gdg.pium.global.security;

import gdg.pium.domain.user.entity.User;
import gdg.pium.domain.user.repository.UserRepository;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return CustomUserDetails.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .authorities(
                Collections.singletonList(
                    new SimpleGrantedAuthority(
                        user.getRole().getAuthority()
                    )
                )
            )
            .build();
    }
}
