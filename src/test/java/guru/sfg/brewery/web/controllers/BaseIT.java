package guru.sfg.brewery.web.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.security.google.Google2faFilter;
import guru.sfg.brewery.services.BeerOrderService;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseIT {
    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @MockBean
    BeerOrderService beerOrderService;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }
    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("spring" , "mateusz"),
                Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamAllMinusUser() {
        return Stream.of(Arguments.of("spring" , "mateusz"),
                Arguments.of("scott", "tiger"));
    }
    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("user" , "password"),
                Arguments.of("scott", "tiger"));
    }
}
