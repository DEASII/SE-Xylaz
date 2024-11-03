package ku.cs.xylaz.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class AppointmentRequest {
    private String username;
    private UUID barberId;
    private LocalDateTime appointmentDate;
    private String serviceType;

}
