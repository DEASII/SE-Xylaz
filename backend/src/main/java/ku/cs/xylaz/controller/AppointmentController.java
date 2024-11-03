package ku.cs.xylaz.controller;

import ku.cs.xylaz.entity.Appointment;
import ku.cs.xylaz.entity.Barber;
import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.repository.AppointmentRepository;
import ku.cs.xylaz.repository.BarberRepository;
import ku.cs.xylaz.repository.MemberRepository;
import ku.cs.xylaz.request.AppointmentRequest;
import ku.cs.xylaz.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping("/{docId}")
    public ResponseEntity<?> bookAppointment(@PathVariable String docId, @RequestBody AppointmentRequest request) {
        try {
            Appointment savedAppointment = appointmentService.bookAppointment(docId, request);
            return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("เกิดข้อผิดพลาดในการจองนัดหมาย");
        }
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
    @GetMapping
    public List<Map<String, Object>> getAllAppointmentData() {
        return appointmentRepository.findAll().stream()
                .map(appointment -> {
                    Map<String, Object> appointmentData = new HashMap<>();
                    String appointmentDateTime = appointment.getAppointmentDate(); // ได้ในรูปแบบ "2024-11-05 10:30"

                    // แยกวันที่และเวลา
                    String[] dateTimeParts = appointmentDateTime.split(" ");
                    String date = dateTimeParts[0]; // ได้วันที่
                    String time = dateTimeParts[1]; // ได้เวลา

                    appointmentData.put("appointmentDate", date);
                    appointmentData.put("appointmentTime", time);
                    appointmentData.put("barberId", appointment.getBarber().getId());
                    return appointmentData;
                })
                .collect(Collectors.toList());
    }

}



