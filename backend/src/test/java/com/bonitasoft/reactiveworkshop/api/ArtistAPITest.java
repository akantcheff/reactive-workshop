package com.bonitasoft.reactiveworkshop.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetArtistCommentsNominal() throws Exception {
        mockMvc.perform(get("/artist/{artistId}/comments", "a5c59c0315098e6d67bb57610f7fb9b1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId", is("a5c59c0315098e6d67bb57610f7fb9b1")))
                .andExpect(jsonPath("$.artistName", is("Adele")))
                .andExpect(jsonPath("$.genre", is("Pop")))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments", hasSize(lessThanOrEqualTo(10))))
                .andExpect(jsonPath("$.comments[0].userName").exists())
                .andExpect(jsonPath("$.comments[0].comment").exists());
    }

    @Test
    public void testGetArtistCommentsErrorArtistNotFound() throws Exception {
        mockMvc.perform(get("/artist/{artistId}/comments", "foobar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
