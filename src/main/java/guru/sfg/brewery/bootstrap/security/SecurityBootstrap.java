package guru.sfg.brewery.bootstrap.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityBootstrap implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count()==0){
            loadData();
        }
    }

    private void loadData(){
        Authority user = Authority.builder().role("ROLE_USER").build();
        Authority admin = Authority.builder().role("ROLE_ADMIN").build();
        Authority customer = Authority.builder().role("ROLE_CUSTOMER").build();

       Authority savedUser =authorityRepository.save(user);
       Authority savedAdmin =authorityRepository.save(admin);
       Authority savedCustomer= authorityRepository.save(customer);

        User spring = User.builder().authority(savedAdmin).username("spring").password("{bcrypt}$2a$10$iXjK0OOCjOUpnAXjwlxv2eEp4Kq5/je9c2J0EO1e1Oha.xlwerhcy").build();

        userRepository.save(spring);
        User user1 = User.builder().username("user").authority(savedUser).password("{sha256}695cca3adede6a3e22ed74ae610adb654d8f55330cbd445a01797b2142259d6e0f7dc8d4f693da0b").build();

        userRepository.save(user1);

        User scott = User.builder().username("scott").authority(savedCustomer).password("{bcrypt15}$2a$15$cPt7VK1GivOqKnOe0z/nt.pvCk4I7AwmKsb/ejhu6RHMQFzB4/MYS").build();

        userRepository.save(scott);


    }
}
