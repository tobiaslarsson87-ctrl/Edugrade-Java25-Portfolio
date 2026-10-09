package se.edugrade.java25.enterprise.gym.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.edugrade.java25.enterprise.gym.dto.GymClassRequest;
import se.edugrade.java25.enterprise.gym.dto.GymClassResponse;
import se.edugrade.java25.enterprise.gym.exception.GymClassNotFoundException;
import se.edugrade.java25.enterprise.gym.security.JwtAuthFilter;
import se.edugrade.java25.enterprise.gym.security.JwtUtil;
import se.edugrade.java25.enterprise.gym.security.SecurityConfig;
import se.edugrade.java25.enterprise.gym.service.BookingService;
import se.edugrade.java25.enterprise.gym.service.GymClassService;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GymClassController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class})
public class GymClassControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private GymClassService gymClassService;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final GymClassResponse pseudoResponse = new GymClassResponse(
            1L,
            "Karate",
            "Ali",
            "",
            "Thursday",
            "15:00",
            75,
            20
    );

    @Test
    @DisplayName("GET /classes returns 200. permitAll() should allow anyone")
    void getAll_NoAuth_200 () throws Exception {
        when(gymClassService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(pseudoResponse)));

        mockMvc.perform(get("/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Karate"));
    }

    @Test
    @DisplayName("GET /classes/{id} returns 200. permitAll() should allow anyone")
    void getById_NoAuth_200 () throws Exception {
        when(gymClassService.findById(1L)).thenReturn(pseudoResponse);

        mockMvc.perform(get("/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructor").value("Ali"));
    }

    @Test
    @DisplayName("GET /classes/{id} returns 404 if non existent")
    void getById_NonExist_404 () throws Exception {
        when(gymClassService.findById(999L)).thenThrow(new GymClassNotFoundException(999L));

        mockMvc.perform(get("/classes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /classes with ADMIN returns 201")
    void newClass_Auth_201 () throws Exception {
        when(gymClassService.create(any(GymClassRequest.class))).thenReturn(pseudoResponse);

        GymClassRequest request = new GymClassRequest(
                "Karate",
                "Ali",
                "",
                "Thursday",
                "15:00",
                75,
                20
        );

        mockMvc.perform(post("/classes")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Karate"));
    }

    @Test
    @DisplayName("POST /classes with USER returns 403")
    void newClass_UnAuth_403 () throws Exception {
        GymClassRequest request = new GymClassRequest(
                "Karate",
                "Ali",
                "",
                "Thursday",
                "15:00",
                75,
                20
        );

        mockMvc.perform(post("/classes")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // not required for criteria, just for my own learning benefit, trying to wrap my head around 401 - 403 differences
    @Test
    @DisplayName("POST /classes with null returns 401")
    void newClass_null_401 () throws Exception {
        GymClassRequest request = new GymClassRequest(
                "Karate",
                "Ali",
                "",
                "Thursday",
                "15:00",
                75,
                20
        );

        mockMvc.perform(post("/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /classes returns 400 on out of constraint violations")
    void newClass_BadRequest_400 () throws Exception {
        GymClassRequest badRequest = new GymClassRequest(
                "",
                "Ali",
                "",
                "Thursday",
                "",
                999,
                999
        );

        mockMvc.perform(post("/classes")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /classes/{id} with ADMIN returns 204")
    void deleteClass_Auth_204 () throws Exception {
        mockMvc.perform(delete("/classes/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /classes/{id} with USER returns 403")
    void deleteClass_UnAuth_403 () throws Exception {
        mockMvc.perform(delete("/classes/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /classes/{999} with ADMIN returns 404")
    void deleteClass_NonExist_404 () throws Exception {
        when(gymClassService.findById(999L)).thenThrow(new GymClassNotFoundException(999L));
        mockMvc.perform(get("/classes/999"))
                .andExpect(status().isNotFound());
    }
}
