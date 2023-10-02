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
public class Account {

    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String account_created;
    private String account_updated;
}
