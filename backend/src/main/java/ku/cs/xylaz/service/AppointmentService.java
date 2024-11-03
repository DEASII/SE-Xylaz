package ku.cs.xylaz.service;

import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.entity.Appointment;
import ku.cs.xylaz.repository.AppointmentRepository;
import ku.cs.xylaz.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, MemberRepository memberRepository) {
        this.appointmentRepository = appointmentRepository;
        this.memberRepository = memberRepository;
    }
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public List<Appointment> getAppointmentsByUserId(UUID userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return appointmentRepository.findByMember(member);
    }
    public List<Appointment> getAppointmentsByMember(Member member) {
        return appointmentRepository.findByMember(member); // ใช้ฟังก์ชันที่สร้างไว้
    }
}
