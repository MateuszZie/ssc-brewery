package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BreweryControllerTest extends BaseIT{


    @Test
    void getBreweriesJsonWithCustomerRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries")
        .with(httpBasic("scott","tiger")))
                .andExpect(status().is2xxSuccessful());

    }
    @Test
    void getBreweriesJsonWithOutCustomerRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("spring","mateusz")))
                .andExpect(status().isForbidden());

    }
    @Test
    void getBreweriesJsonNoAuthorize() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());

    }
}