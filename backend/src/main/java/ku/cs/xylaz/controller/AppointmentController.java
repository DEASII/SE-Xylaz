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
    @PatchMapping
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable UUID id, @RequestBody Appointment appointmentDetails) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(appointmentDetails.getStatus()); // อัปเดตสถานะ

            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // ถ้าไม่พบการนัดหมาย
        }
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
                    appointmentData.put("barberProfilePicture", appointment.getBarber().getProfilePicture());
                    appointmentData.put("barberName", appointment.getBarber().getName());
                    appointmentData.put("username",appointment.getMember().getUsername());
                    appointmentData.put("appointmentId",appointment.getId());
                    return appointmentData;
                })
                .collect(Collectors.toList());
    }


}



