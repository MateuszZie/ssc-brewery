package guru.sfg.brewery.bootstrap.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityBootstrap implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count()==0){
            loadData();
        }
    }

    private void loadData(){

        Authority createBeer = Authority.builder().permission("create.beer").build();
        Authority updateBeer = Authority.builder().permission("update.beer").build();
        Authority readBeer = Authority.builder().permission("read.beer").build();
        Authority deleteBeer = Authority.builder().permission("delete.beer").build();

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN_ROLE").build());
        Role userRole = roleRepository.save(Role.builder().name("USER_ROLE").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER_ROLE").build());

        adminRole.setAuthorities(Set.of(readBeer,deleteBeer,updateBeer,createBeer));
        userRole.setAuthorities(Set.of(readBeer));
        customerRole.setAuthorities(Set.of(readBeer));

        roleRepository.saveAll(Arrays.asList(adminRole,userRole,customerRole));

        User spring = User.builder().role(adminRole).username("spring").password("{bcrypt}$2a$10$iXjK0OOCjOUpnAXjwlxv2eEp4Kq5/je9c2J0EO1e1Oha.xlwerhcy").build();

        userRepository.save(spring);
        User user1 = User.builder().username("user").role(userRole).password("{sha256}695cca3adede6a3e22ed74ae610adb654d8f55330cbd445a01797b2142259d6e0f7dc8d4f693da0b").build();

        userRepository.save(user1);

        User scott = User.builder().username("scott").role(customerRole).password("{bcrypt15}$2a$15$cPt7VK1GivOqKnOe0z/nt.pvCk4I7AwmKsb/ejhu6RHMQFzB4/MYS").build();

        userRepository.save(scott);


    }
}
