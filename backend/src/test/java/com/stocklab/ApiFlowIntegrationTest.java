package com.stocklab;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiFlowIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }

    @Test
    void signupShouldWork() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "newuser",
                                "password", "Strong123!",
                                "fullName", "New User"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("username", "user1", "password", "User1234!"))))
                .andExpect(status().isOk());
    }

    @Test
    void userCanCreateBuyOrder() throws Exception {
        String token = login("user1", "User1234!");
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "stockId", 1,
                                "side", "BUY",
                                "quantity", 1
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotAccessAdminEndpoint() throws Exception {
        String token = login("user1", "User1234!");
        MvcResult modeResult = mockMvc.perform(get("/api/meta/mode"))
                .andExpect(status().isOk())
                .andReturn();
        String mode = objectMapper.readTree(modeResult.getResponse().getContentAsString()).get("mode").asText();
        ResultMatcher expected = "VULNERABLE".equalsIgnoreCase(mode)
                ? status().isOk()
                : status().isForbidden();
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(expected);
    }

    @Test
    void adminCanCreateStock() throws Exception {
        String token = login("admin1", "Admin1234!");
        mockMvc.perform(post("/api/admin/stocks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "symbol", "LABX",
                                "name", "Lab Stock",
                                "currentPrice", 12.34,
                                "active", true
                        ))))
                .andExpect(status().isOk());
    }
}
