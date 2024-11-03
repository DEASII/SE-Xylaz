package ku.cs.xylaz.service;

import io.jsonwebtoken.ExpiredJwtException;
import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    private String jwtSecret; // เก็บ secret key สำหรับสร้าง JWT

    @Value("${jwt.expiration.ms}")
    private long jwtExpiration; // อายุการใช้งานของ token (มิลลิวินาที)

    public LoginService(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) {
        Member member = repository.findByUsername(username);
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // สร้าง JWT Token
        return generateToken(member);
    }

    private String generateToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("username", member.getUsername());
        claims.put("role", member.getRole());

        // สร้าง Token โดยใช้ JJWT library
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)

                .compact();
    }
    public Boolean validateToken(String token) {
        try {
            // ตรวจสอบว่า token ถูกต้องและยังไม่หมดอายุ
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true; // Token ยัง valid
        } catch (ExpiredJwtException e) {
            // Token หมดอายุ
            System.out.println("Token expired");
            return false; // Token ไม่ valid
        } catch (Exception e) {
            // ข้อผิดพลาดอื่น ๆ
            return false; // Token ไม่ valid
        }
    }

}

