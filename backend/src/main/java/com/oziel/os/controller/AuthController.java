package com.oziel.os.controller;
import com.oziel.os.dto.*;
import com.oziel.os.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtService jwt;
    private final String adminEmail;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private final String adminHash;
    public AuthController(JwtService jwt,@Value("${admin.email}") String adminEmail,@Value("${admin.password}") String adminPassword){
        this.jwt=jwt;this.adminEmail=adminEmail;this.adminHash=encoder.encode(adminPassword);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        if(!adminEmail.equalsIgnoreCase(request.email()) || !encoder.matches(request.password(),adminHash))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new LoginResponse(jwt.generate(request.email()),"Bearer"));
    }
}
