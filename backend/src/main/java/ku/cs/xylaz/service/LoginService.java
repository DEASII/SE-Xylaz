package ku.cs.xylaz.service;

import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {
        Member member = repository.findByUsername(username);
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // สร้าง JWT Token (คุณอาจมีคลาสแยกต่างหากสำหรับการสร้าง Token)
        String token = generateToken(member);
        return token;
    }

    private String generateToken(Member member) {
        // โค้ดสำหรับการสร้าง JWT Token
        // คืนค่า Token ที่สร้างขึ้น
        return "token"; // เปลี่ยนเป็นการสร้าง Token จริง
    }
}
