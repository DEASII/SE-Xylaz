package ku.cs.xylaz.controller;

import ku.cs.xylaz.entity.Appointment;
import ku.cs.xylaz.entity.Barber;
import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.repository.AppointmentRepository;
import ku.cs.xylaz.repository.BarberRepository;
import ku.cs.xylaz.repository.MemberRepository;
import ku.cs.xylaz.request.AppointmentRequest;
import ku.cs.xylaz.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final MemberRepository memberRepository;
    private final BarberRepository barberRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentService appointmentService,
                                 MemberRepository memberRepository,
                                 BarberRepository barberRepository,
                                 AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.memberRepository = memberRepository;
        this.barberRepository = barberRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping("/{docId}")
    public ResponseEntity<Appointment> bookAppointment(@PathVariable String docId, @RequestBody AppointmentRequest request) {
        // Validate request
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ใช้ docId เป็น barberId
        Barber barber = barberRepository.findById(UUID.fromString(docId))
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        // สร้างนัดหมายใหม่
        Appointment newAppointment = new Appointment();
        newAppointment.setMember(member);
        newAppointment.setBarber(barber);
        newAppointment.setAppointmentDate(request.getAppointmentDate());
        newAppointment.setStatus("Confirmed"); // Set initial status
        newAppointment.setServiceType(request.getServiceType());

        // บันทึกนัดหมาย
        Appointment savedAppointment = appointmentRepository.save(newAppointment);

        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }


    @GetMapping("/{docId}")
    public String book(@PathVariable String docId, @RequestBody AppointmentRequest request) {

        return "yo "+request.getMemberId().toString();
    }
    public List<Barber> convertToBarberList(List<Map<String, Object>> barbersData) {
        List<Barber> barbers = new ArrayList<>();

        for (Map<String, Object> barberData : barbersData) {
            Barber barber = new Barber();
            barber.setId(UUID.fromString((String) barberData.get("barber_id"))); // เปลี่ยนให้ตรงกับคีย์ที่ใช้งาน
            barber.setName((String) barberData.get("name")); // เปลี่ยนให้ตรงกับคีย์ที่ใช้งาน
            barber.setSpecialty((String) barberData.get("specialty")); // เปลี่ยนให้ตรงกับคีย์ที่ใช้งาน
            barber.setExperience((Integer) barberData.get("experience")); // เปลี่ยนให้ตรงกับคีย์ที่ใช้งาน
            barber.setProfilePicture((String) barberData.get("profile_picture")); // เปลี่ยนให้ตรงกับคีย์ที่ใช้งาน

            barbers.add(barber);
        }

        return barbers;
    }
//    public List<Barber> getAllBarbers() {
//        List<Map<String, Object>> barbersData = barberRepository.findAll(); // สมมุติว่าเป็นเมธอดที่ส่งคืน List<Map>
//        return convertToBarberList(barbersData);
}



