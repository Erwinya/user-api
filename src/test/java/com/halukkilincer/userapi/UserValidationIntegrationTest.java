package com.halukkilincer.userapi;

import com.halukkilincer.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void rejectBlankAndMalformedFields() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","email":"not-an-email"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void trimBeforeSizeValidation() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" A ","email":" short@example.com "}
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"  Ada  ","email":"  Ada@Example.COM  "}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ada"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void rejectNonPositiveIdAndUpdateConflict() throws Exception {
        mockMvc.perform(get("/api/v1/users/0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"One","email":"one@example.com"}
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Two","email":"two@example.com"}
                                """))
                .andExpect(status().isCreated());

        long id = userRepository.findAllByOrderByIdAsc().get(1).getId();
        mockMvc.perform(put("/api/v1/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Two","email":"one@example.com"}
                                """))
                .andExpect(status().isConflict());
    }
}
