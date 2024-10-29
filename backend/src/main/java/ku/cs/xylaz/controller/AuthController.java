package ku.cs.xylaz.controller;

import jakarta.validation.Valid;
import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.request.LoginRequest;
import ku.cs.xylaz.request.SignupRequest;
import ku.cs.xylaz.service.SignupService;
import ku.cs.xylaz.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private SignupService signupService;
    private final MemberRepository memberRepository;

    // Constructor injection (แนะนำให้ใช้)
    public AuthController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    //     สำหรับเข้าสู่ระบบ
    @PostMapping("/singin")
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        // ตรวจสอบว่ารับ username และ password ครบถ้วนหรือไม่
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        // ค้นหาผู้ใช้จากฐานข้อมูล
        Member member = memberRepository.findByUsername(loginRequest.getUsername());

        // ถ้าผู้ใช้ไม่พบ
        if (member == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // เปรียบเทียบ password
        if (!member.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(400).body("Invalid password");
        }

        // หากเข้าสู่ระบบสำเร็จ
        return ResponseEntity.ok("Login successful for user: " + member.getUsername());
    }

    @RestController
    public class SigninController {

        @PostMapping("/signup")
        public ResponseEntity<String> signupUser(@RequestBody LoginRequest loginRequest) {
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            // ค้นหาผู้ใช้จากฐานข้อมูล
            Member member = memberRepository.findByUsername(loginRequest.getUsername());

            // ถ้าผู้ใช้ไม่พบ
            if (member == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            // เปรียบเทียบ password
            if (!member.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.status(400).body("Invalid password");
            }

            // หากเข้าสู่ระบบสำเร็จ
            return ResponseEntity.ok("Login successful for user: ");
        }

        @RestController
        public class SignupController {

            @GetMapping("/signin")
            public String signupUser(@RequestBody LoginRequest loginRequest) {

                // ค้นหาผู้ใช้จากฐานข้อมูล
                Member member = memberRepository.findByUsername(loginRequest.getUsername());
               return member.getName();
            }


        }
    }
}