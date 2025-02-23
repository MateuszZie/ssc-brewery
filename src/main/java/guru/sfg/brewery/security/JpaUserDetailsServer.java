package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsServer implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(()->{
            return new UsernameNotFoundException("User name " +s + " not found");
        });
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
//                user.isAccountNonLocked(), convertToSpringAuthorities(user.getAuthorities()));
    }

//    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
//        if(authorities != null && authorities.size() > 0){
//            return authorities.stream()
//                    .map(Authority::getPermission)
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toSet());
//        }else {
//            return new HashSet<>();
//        }
//    }
}
