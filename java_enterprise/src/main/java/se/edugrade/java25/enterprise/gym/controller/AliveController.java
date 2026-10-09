package se.edugrade.java25.enterprise.gym.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AliveController {
    @GetMapping("/alive")
    public ResponseEntity<Map<String, String>> alive() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", "UP");
        return ResponseEntity.ok(body);
    }
}
