package tech.stark.domain;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
public class Assignment {

    private String id;
    private String name;
    private int points;
    private int num_of_attempts;
    private String deadline;
    private String assignment_created;
    private String assignment_updated;
}
