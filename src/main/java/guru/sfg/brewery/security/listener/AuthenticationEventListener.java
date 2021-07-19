package guru.sfg.brewery.security.listener;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.LoginSuccess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailedRepository;
import guru.sfg.brewery.repositories.security.LoginSuccessRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final LoginSuccessRepository successRepository;
    private final LoginFailedRepository failedRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationSuccessEvent event){

        log.debug("User Logg in OK");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            LoginSuccess builder = LoginSuccess.builder().build();

            if(token.getPrincipal() instanceof User){
                User user = (User) token.getPrincipal();
                builder.setUser(user);
                log.debug("User name logged in: " + user.getUsername() );
            }

            if(token.getDetails() instanceof WebAuthenticationDetails){
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                builder.setSourceIp(details.getRemoteAddress());
                log.debug("Source IP: " + details.getRemoteAddress());
            }
            LoginSuccess loginSuccess = successRepository.save(builder);
            log.debug("Login success id: " + loginSuccess.getId());
        }

    }
    @EventListener
    public void listenFail(AuthenticationFailureBadCredentialsEvent event){

        log.debug("User Logg in Fail");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
            if (token.getPrincipal() instanceof String){
                log.debug("Failed User name: " + token.getPrincipal());
                if(userRepository.findByUsername( (String) token.getPrincipal()).isPresent()){
                   builder.user(userRepository.findByUsername( (String) token.getPrincipal()).get());
                   log.debug("User saved");
                }
            }

            if(token.getDetails() instanceof WebAuthenticationDetails){
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();

                log.debug("Source IP: " + details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());
            }
            LoginFailure failed = failedRepository.save(builder.build());
            log.debug("Failed login id: " +failed.getId());

            if(failed.getUser()!=null){
                lockUserAccount(failed.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
        List<LoginFailure> loginFailures = failedRepository.findAllByUserAndCreatedDateIsAfter(user,
                Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        if(loginFailures.size()>3){
            log.debug("Account locked");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }

    }

}
