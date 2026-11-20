package com.oziel.os.config;
import com.oziel.os.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtFilter;
    @Bean
    SecurityFilterChain security(HttpSecurity http)throws Exception{
        http.cors(c->c.configurationSource(cors())).csrf(csrf->csrf.disable())
          .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(a->a
            .requestMatchers("/api/v1/auth/login","/api/v1/health","/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**","/h2-console/**").permitAll()
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated())
          .headers(h->h.frameOptions(f->f.disable()))
          .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    private CorsConfigurationSource cors(){
        var config=new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "https://*.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        var source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }
}
