package tech.stark.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    private String name;
    private int points;
    private int num_of_attempts;
    private String deadline;
    private String assignment_created;
    private String assignment_updated;
    @JsonIgnore
    private String user_email;
}
