package fr.uit.univparis8.tpair.tpair1.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uit.univparis8.tpair.tpair1.api.annonce.dto.AnnonceRequest;
import fr.uit.univparis8.tpair.tpair1.enums.Role;
import fr.uit.univparis8.tpair.tpair1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnnonceControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    private String registerAndLogin() throws Exception {
        var payload = Map.of(
                "username", "bob",
                "email", "bob@example.com",
                "password", "pass1234"
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "bob",
                                "password", "pass1234"
                        ))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resp).get("token").asText();
    }

    private String registerAndLoginAs(String username) throws Exception {
        var payload = Map.of(
                "username", username,
                "email", username + "@example.com",
                "password", "pass1234"
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "password", "pass1234"
                        ))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resp).get("token").asText();
    }

    private long createAnnonce(String token, String title) throws Exception {
        AnnonceRequest req = new AnnonceRequest();
        req.setTitle(title);
        req.setDescription("Desc");
        req.setAdress("Adresse");
        req.setMail("user@example.com");

        String resp = mockMvc.perform(post("/api/annonces")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    void createAndListAnnonce() throws Exception {
        String token = registerAndLogin();

        AnnonceRequest req = new AnnonceRequest();
        req.setTitle("Titre");
        req.setDescription("Desc");
        req.setAdress("Adresse");
        req.setMail("user@example.com");

        mockMvc.perform(post("/api/annonces")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/annonces")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Titre"));
    }

    @Test
    void shouldReturn401WhenNoToken() throws Exception {
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void ownerOnlyCanModifyAndDelete() throws Exception {
        String tokenOwner = registerAndLoginAs("owner");
        long annonceId = createAnnonce(tokenOwner, "Owned");

        String tokenIntruder = registerAndLoginAs("intruder");

        AnnonceRequest update = new AnnonceRequest();
        update.setTitle("Hack");
        update.setDescription("Desc");
        update.setAdress("Adresse");
        update.setMail("user@example.com");

        mockMvc.perform(put("/api/annonces/" + annonceId)
                        .header("Authorization", "Bearer " + tokenIntruder)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/annonces/" + annonceId)
                        .header("Authorization", "Bearer " + tokenIntruder))
                .andExpect(status().isForbidden());
    }

    @Test
    void cannotUpdateWhenPublished_andDeleteNeedsArchive() throws Exception {
        String token = registerAndLoginAs("alice");
        long annonceId = createAnnonce(token, "PubTest");

        mockMvc.perform(patch("/api/annonces/" + annonceId + "/publish")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        AnnonceRequest upd = new AnnonceRequest();
        upd.setTitle("New");
        upd.setDescription("Desc");
        upd.setAdress("Adresse");
        upd.setMail("user@example.com");
        mockMvc.perform(put("/api/annonces/" + annonceId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/api/annonces/" + annonceId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/annonces/" + annonceId + "/archive")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        String adminToken = registerAndPromoteAdmin("admin");

        mockMvc.perform(patch("/api/annonces/" + annonceId + "/archive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/annonces/" + annonceId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private String registerAndPromoteAdmin(String username) throws Exception {
        var payload = Map.of(
                "username", username,
                "email", username + "@example.com",
                "password", "pass1234"
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        userRepository.findByUsername(username).ifPresent(u -> {
            u.setRole(Role.ADMIN);
            userRepository.save(u);
        });

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "password", "pass1234"
                        ))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resp).get("token").asText();
    }
}
