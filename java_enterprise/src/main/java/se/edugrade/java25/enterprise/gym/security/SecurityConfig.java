package se.edugrade.java25.enterprise.gym.security;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(c -> c.disable())
        .authorizeHttpRequests(a -> a
                //MICS
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/alive").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                //PUBLIC
                .requestMatchers(HttpMethod.GET,"/classes").permitAll()
                .requestMatchers(HttpMethod.GET,"/classes/*").permitAll()
                .requestMatchers(HttpMethod.GET,"/classes/*/bookings").permitAll()
                .requestMatchers(HttpMethod.GET,"/classes/*/spots-remaining").permitAll()
                .requestMatchers(HttpMethod.GET,"/classes/available").permitAll()
                .requestMatchers(HttpMethod.GET, "/classes/search").permitAll()
                //ADMIN & USER
                .requestMatchers(HttpMethod.POST, "/classes/*/bookings").hasAnyRole("ADMIN", "USER")
                //ADMIN ONLY
                .requestMatchers(HttpMethod.POST, "/classes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/classes/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/classes/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/bookings/*").hasRole("ADMIN")
                .anyRequest().denyAll() //only endpoints with EXPLICIT permissions should authenticate or be permitted
        )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthenticated\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"forbidden\": \"Unauthorized\"}");
                        })
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
