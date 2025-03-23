package gdg.pium.global.security;

import gdg.pium.domain.account.entity.Account;
import gdg.pium.domain.account.repository.AccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return CustomUserDetails.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .password(account.getPassword())
            .authorities(
                Collections.singletonList(
                    new SimpleGrantedAuthority(
                        account.getRole().getAuthority()
                    )
                )
            )
            .build();
    }
}
