package ku.cs.xylaz.controller;

import ku.cs.xylaz.response.ApiResponse;
import ku.cs.xylaz.request.LoginRequest;
import ku.cs.xylaz.request.SignupRequest;
import ku.cs.xylaz.response.LoginResponse;
import ku.cs.xylaz.service.LoginService;
import ku.cs.xylaz.service.SignupService;
import ku.cs.xylaz.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    //    // สำหรับลงทะเบียน
    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@RequestBody SignupRequest user) {

        if (signupService.isUsernameAvailable(user.getUsername())) {
            signupService.createUser(user);
            return ResponseEntity.ok("Signup successful");
        } else {
            return ResponseEntity.status(400).body("Username not available");
        }
    }

    @Autowired
    private LoginService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // ตรวจสอบ username และ password แล้วสร้าง token
            String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            // สร้าง Response ที่มี token
            LoginResponse loginResponse = new LoginResponse(token);
            ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(true, "Sign-in successful", loginResponse);

            // ส่ง Response กลับ
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Sign-in failed: " + e.getMessage(), null));
        }

}

//    @RestController
//    public class SigninController {
//        @Autowired
//        private PasswordEncoder passwordEncoder;
//        @PostMapping("/signin")
//        public ResponseEntity<String> signupUser(@RequestBody LoginRequest loginRequest) {
//            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
//                return ResponseEntity.badRequest().body("Username and password are required");
//            }
//
//            // ค้นหาผู้ใช้จากฐานข้อมูล
//            Member member = memberRepository.findByUsername(loginRequest.getUsername());
//
//            // ถ้าผู้ใช้ไม่พบ
//            if (member == null) {
//                return ResponseEntity.status(404).body("User not found");
//            }
//
//            // เปรียบเทียบ password
//            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
//                return ResponseEntity.status(400).body("Invalid password");
//            }
//
//            // หากเข้าสู่ระบบสำเร็จ
//            return ResponseEntity.ok("Login successful for user: ");
//        }

//        @RestController
//        public class SignupController {
//
//            @GetMapping("/signin")
//            public String signupUser(@RequestBody LoginRequest loginRequest) {
//
//                // ค้นหาผู้ใช้จากฐานข้อมูล
//                Member member = memberRepository.findByUsername(loginRequest.getUsername());
//               return member.getName();
//            }
//
//
//        }

}