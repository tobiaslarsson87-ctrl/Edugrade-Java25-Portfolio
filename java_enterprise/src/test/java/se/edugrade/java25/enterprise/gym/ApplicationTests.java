package se.edugrade.java25.enterprise.gym;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // performs auth/login: username, password and returns the token as a String
    private String login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("token").asText();
    }

    @Test
    @DisplayName("Login -> create GymClass with real ADMIN TOKEN -> 201")
    void loginAndCreateNewGymClass_WithAdminToken () throws Exception {
        String token = login("admin", "password");

        String json = """
        {
            "name": "Fencing",
            "instructor": "George",
            "description": "Cool",
            "dayOfWeek": "Friday",
            "startTime": "10:00",
            "durationMinutes": 45,
            "maxParticipants": 8
        }
        """;

        mockMvc.perform(post("/classes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fencing"));
    }

    @Test
    @DisplayName("POST /classes without NO TOKEN -> 401")
    void unauthenticated_createGymClass () throws Exception {
        String json = """
        {
            "name": "Fencing",
            "instructor": "George",
            "description": "Cool",
            "dayOfWeek": "Friday",
            "startTime": "10:00",
            "durationMinutes": 45,
            "maxParticipants": 8
        }
        """;

        mockMvc.perform(post("/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE /classes -> GET returns 404")
    void authorized_delteGymclass () throws Exception {
        String token = login("admin", "password");

        String json = """
        {
            "name": "Fencing",
            "instructor": "George",
            "description": "Cool",
            "dayOfWeek": "Friday",
            "startTime": "10:00",
            "durationMinutes": 45,
            "maxParticipants": 8
        }
        """;

        MvcResult result = mockMvc.perform(post("/classes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        //DELETE
        mockMvc.perform(delete("/classes/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        //SHOULD NO LONGER EXIST
        mockMvc.perform(get("/classes/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /classes/{id}/bookings -> FULL CLASS returns 409")
    void bookingOnFullClass () throws Exception {
        String token = login("admin", "password");

        String gymClass = """
    {
        "name": "Single",
        "instructor": "Loner",
        "description": "Privacy",
        "dayOfWeek": "Saturday",
        "startTime": "23:00",
        "durationMinutes": 15,
        "maxParticipants": 1
    }
    """;

        String booking = """
    {
        "participantName": "LoneWolf33",
        "email": "lonewolf_33@gmail.com"
    }
    """;

        //CREATE - class only has 1 spot
        MvcResult result = mockMvc.perform(post("/classes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gymClass))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        // SINGLE BOOKING - is fine
        mockMvc.perform(post("/classes/" + id + "/bookings")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(booking))
                .andExpect(status().isCreated());

        // SINGLE BOOKING - should return 409
        mockMvc.perform(post("/classes/" + id + "/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(booking))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Auth/register -> Auth/login -> VALID TOKEN")
    void registerAndLogin_GivesToken () throws Exception {
        String credentials = """
                    {
                        "username": "tob123",
                        "password": "hejhejhej999777"
                    }
                    """;

        //REGISTER
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials))
                .andExpect(status().isCreated());

        //LOGIN
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isOk())
                .andReturn();

        //GET TOKEN
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String token = json.get("token").asText();

        //CHECK NOT EMPTY
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Auth/register -> Auth/login -> POST /classes -> 403")
    void registerAndLogin_User_CanNotCreateGymClass () throws Exception {
        String credentials = """
                    {
                        "username": "tob9999",
                        "password": "testtest1111"
                    }
                    """;

        //REGISTER
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isCreated());

        //LOGIN
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials))
                .andExpect(status().isOk())
                .andReturn();

        //GET TOKEN
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String token = json.get("token").asText();

        //GYMCLASS
        String gymClass = """
    {
        "name": "Single",
        "instructor": "Loner",
        "description": "Privacy",
        "dayOfWeek": "Saturday",
        "startTime": "23:00",
        "durationMinutes": 15,
        "maxParticipants": 1
    }
    """;
        //TRY TO CREATE
        mockMvc.perform(post("/classes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gymClass))
                .andExpect(status().isForbidden());
    }
}
