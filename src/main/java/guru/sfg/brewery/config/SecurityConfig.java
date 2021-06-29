package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/","/webjars/**","/login","/resources/**").permitAll()
                            .antMatchers("/beers/find","/beers*").permitAll()
                            .antMatchers(HttpMethod.GET ,"/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$iXjK0OOCjOUpnAXjwlxv2eEp4Kq5/je9c2J0EO1e1Oha.xlwerhcy")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}695cca3adede6a3e22ed74ae610adb654d8f55330cbd445a01797b2142259d6e0f7dc8d4f693da0b")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{ldap}{SSHA}A2kAYsgelm08I3wlEHHk1+L/YMolCJse5ai1CA==")
                .roles("CUSTOMER");
    }
    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("mateusz")
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        List<UserDetails> userDetails = new ArrayList<>();
//        userDetails.add(admin);
//        userDetails.add(user);
//        return new InMemoryUserDetailsManager(userDetails);
//    }
}
