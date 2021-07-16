package guru.sfg.brewery.security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {

    @EventListener
    public void listen(AuthenticationSuccessEvent authenticationSuccessEvent){

        log.debug("User Logg in OK");

    }
}
