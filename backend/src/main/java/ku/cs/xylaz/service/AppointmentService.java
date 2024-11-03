package ku.cs.xylaz.service;

import ku.cs.xylaz.entity.Member;
import ku.cs.xylaz.entity.Appointment;
import ku.cs.xylaz.repository.AppointmentRepository;
import ku.cs.xylaz.repository.MemberRepository;
import ku.cs.xylaz.request.AppointmentRequest;
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
//    public void createAppointment(AppointmentRequest appointmentRequest) {
//        if (appointmentRequest.getMemberId() == null || appointmentRequest.getBarberId() == null) {
//            throw new IllegalArgumentException("Member ID and Barber ID cannot be null");
//        }
//
//        Appointment appointment = new Appointment();
//        appointment.setMemberId(appointmentRequest.getMemberId());
//        appointment.setBarberId(appointmentRequest.getBarberId());
//        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
//        appointment.setServiceType(appointmentRequest.getServiceType());
//
//        appointmentRepository.save(appointment);
//    }


    public List<Appointment> getAppointmentsByUserId(UUID userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return appointmentRepository.findByMember(member);
    }
    public List<Appointment> getAppointmentsByMember(Member member) {
        return appointmentRepository.findByMember(member); // ใช้ฟังก์ชันที่สร้างไว้
    }
}
