package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Disabled
@WebMvcTest
public class IndexControllerIT extends BaseIT {

    @Test
    void testGetIndexSlash() throws Exception{
        mockMvc.perform(get("/" ))
                .andExpect(status().isOk());
    }
    @Test
    void testFindBeers() throws Exception{
        mockMvc.perform(get("/beers/find" ))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void findBeerByUpc() throws Exception{
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isUnauthorized());
    }
}
