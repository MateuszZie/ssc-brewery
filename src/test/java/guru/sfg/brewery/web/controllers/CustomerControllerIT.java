package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT extends BaseITJpa {

    @Test
    void listCustomerNoAuth() throws Exception{
        mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void listCustomersUser() throws Exception{
        mockMvc.perform(get("/customers")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }
    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamAllMinusUser")
    void listCustomersCusAndAdmin(String user, String pwd) throws Exception{
        mockMvc.perform(get("/customers")
                .with(httpBasic(user, pwd)))
                .andExpect(status().is2xxSuccessful());
    }
    @Rollback
    @Test
    void processCreationForm() throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName", "Foo Customer")
                .with(httpBasic("spring", "mateusz")))
                .andExpect(status().is3xxRedirection());
    }

    @Rollback
    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
    void processCreationFormNOTAUTH(String user, String pwd) throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName", "Foo Customer2")
                .with(httpBasic(user, pwd)))
                .andExpect(status().isForbidden());
    }

    @Test
    void processCreationFormNOAUTH() throws Exception{
        mockMvc.perform(post("/customers/new")
                .param("customerName", "Foo Customer"))
                .andExpect(status().isUnauthorized());
    }
}
