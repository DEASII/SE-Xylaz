package ku.cs.xylaz.controller;

import ku.cs.xylaz.entity.Barber;
import ku.cs.xylaz.repository.BarberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BarberController {

    private final BarberRepository barberRepository;

    public BarberController(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    @PostMapping("/addBarber")
    public ResponseEntity<Barber> addBarber(@RequestBody Barber barber) {
        System.out.println("Adding barber: " + barber);
        Barber savedBarber = barberRepository.save(barber);
        return new ResponseEntity<>(savedBarber, HttpStatus.CREATED);
    }
    @GetMapping("/")
    public List<Map<String, Object>> getAllBarberData() {
        return barberRepository.findAll().stream()
                .map(barber -> {
                    // สร้างแผนที่ข้อมูลเพื่อส่งไปยัง Frontend
                    Map<String, Object> barberData = new HashMap<>();
                    barberData.put("barber_id", barber.getId()); // ใช้ barber ID ที่คุณต้องการ
                    barberData.put("name", barber.getName());
                    barberData.put("specialty", barber.getSpecialty());
                    barberData.put("experience", barber.getExperience());
                    barberData.put("profilePicture", "doc1");// แก้รูป
                    barberData.put("gender", barber.getGender());
                    barberData.put("about", barber.getAbout());
                    return barberData;
                })
                .collect(Collectors.toList());
    }

}
