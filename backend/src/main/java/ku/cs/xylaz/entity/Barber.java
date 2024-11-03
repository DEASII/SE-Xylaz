package ku.cs.xylaz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barbers")
public class Barber {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "barber_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String specialty;
    private int experience; // จำนวนปีของประสบการณ์
    private String profilePicture;
    private String gender;
    private String about;
}
