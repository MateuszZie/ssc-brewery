package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BeerOrderAuthenticationManager {

    public boolean CustomerIDMatcher(Authentication authentication, UUID customerId){
        User AuthenticationUser = (User) authentication.getPrincipal();

        log.debug("Auth customer id " +AuthenticationUser.getCustomer().getId() + " customer id " + customerId);

        return AuthenticationUser.getCustomer().getId().equals(customerId);
    }
}
