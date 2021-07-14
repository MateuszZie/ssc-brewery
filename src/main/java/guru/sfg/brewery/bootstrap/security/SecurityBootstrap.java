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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityBootstrap implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count()==0){
            loadData();
        }
    }

    private void loadData(){

        Authority createBeer = authorityRepository.save(Authority.builder().permission("create.beer").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("update.beer").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("read.beer").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("delete.beer").build());

        Authority createCustomer = authorityRepository.save(Authority.builder().permission("create.Customer").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("update.Customer").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("read.Customer").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("delete.Customer").build());

        Authority createBrewery = authorityRepository.save(Authority.builder().permission("create.Brewery").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("update.Brewery").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("read.Brewery").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("delete.Brewery").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN_ROLE").build());
        Role userRole = roleRepository.save(Role.builder().name("USER_ROLE").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER_ROLE").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(readBeer,deleteBeer,updateBeer,createBeer,createBrewery,createCustomer,updateBrewery,
                updateCustomer, readBrewery, readCustomer, deleteBrewery, deleteCustomer)));
        userRole.setAuthorities(new HashSet(Set.of(readBeer)));
        customerRole.setAuthorities(new HashSet(Set.of(readBeer, readBrewery, readCustomer)));

        roleRepository.saveAll(Arrays.asList(adminRole,userRole,customerRole));

        User spring = User.builder().role(adminRole).username("spring").password("{bcrypt}$2a$10$iXjK0OOCjOUpnAXjwlxv2eEp4Kq5/je9c2J0EO1e1Oha.xlwerhcy").build();

        userRepository.save(spring);
        User user1 = User.builder().username("user").role(userRole).password("{sha256}695cca3adede6a3e22ed74ae610adb654d8f55330cbd445a01797b2142259d6e0f7dc8d4f693da0b").build();

        userRepository.save(user1);

        User scott = User.builder().username("scott").role(customerRole).password("{bcrypt15}$2a$15$cPt7VK1GivOqKnOe0z/nt.pvCk4I7AwmKsb/ejhu6RHMQFzB4/MYS").build();

        userRepository.save(scott);


    }
}
