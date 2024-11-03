package ku.cs.xylaz.config;

import jakarta.servlet.http.HttpServletResponse;
import ku.cs.xylaz.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable()) // ปิด CSRF
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/signup", "/login").permitAll() // Allow these paths
//                        .requestMatchers("/menus/add", "/categories/add", "/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated() // Any other requests need to be authenticated
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "remember-me")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable()) // ปิด CSRF ด้วยวิธีใหม่
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // ใช้ CorsConfigurationSource ที่กำหนดเอง
//
//        return http.build();
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers("/", "/signup", "/signin","/appointment/*","/addBarber","/appointment").permitAll()
                    .requestMatchers("/menus/add", "/categories/add", "/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginProcessingUrl("/login") // ใช้ URL สำหรับ POST ที่ต้องการ
                    .successHandler((request, response, authentication) -> {
                        // คุณสามารถส่ง response เป็น JSON ได้ที่นี่หากต้องการ
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Login successful!");
                        response.getWriter().flush();
                    })
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

    return http.build();
}

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/h2-console/**");
    }

    // CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow the frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow cookies if needed
//        configuration.setAllowedOrigins(Arrays.asList("*")); // ใช้ชั่วคราวสำหรับการทดสอบ


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
