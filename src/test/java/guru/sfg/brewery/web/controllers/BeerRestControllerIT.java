package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT{

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/")).andExpect(status().isOk());
    }

    @Test
    void findBeersById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/825ab7e8-2163-4165-bcbe-d10491c5c524")).andExpect(status().isOk());
    }

    @Test
    void findBeersUpc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036")).andExpect(status().isOk());
    }
}
