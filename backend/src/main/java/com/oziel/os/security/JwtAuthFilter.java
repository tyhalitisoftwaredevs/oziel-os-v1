package com.oziel.os.security;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    public JwtAuthFilter(JwtService jwt){this.jwt=jwt;}
    @Override
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String auth=req.getHeader("Authorization");
        if(auth!=null && auth.startsWith("Bearer ")) {
            try {
                String subject=jwt.subject(auth.substring(7));
                var authentication=new UsernamePasswordAuthenticationToken(subject,null,List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch(Exception ignored) {}
        }
        chain.doFilter(req,res);
    }
}
