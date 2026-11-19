package com.halukkilincer.userapi;

import com.halukkilincer.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void seed() throws Exception {
        userRepository.deleteAll();
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ada Lovelace","email":"ada@example.com"}
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alan Turing","email":"alan@lab.example"}
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Grace Hopper","email":"grace@example.com"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void filterByNameCaseInsensitive() throws Exception {
        mockMvc.perform(get("/api/v1/users").param("name", "ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("ada@example.com"));
    }

    @Test
    void filterByEmailAndCombined() throws Exception {
        mockMvc.perform(get("/api/v1/users").param("email", "EXAMPLE.COM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get("/api/v1/users")
                        .param("name", "Grace")
                        .param("email", "example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Grace Hopper"));
    }

    @Test
    void emptyFilterResultAndStableOrder() throws Exception {
        mockMvc.perform(get("/api/v1/users").param("name", "no-such-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Ada Lovelace"))
                .andExpect(jsonPath("$[2].name").value("Grace Hopper"));
    }

    @Test
    void rejectOversizedFilter() throws Exception {
        mockMvc.perform(get("/api/v1/users").param("name", "x".repeat(81)))
                .andExpect(status().isBadRequest());
    }
}
