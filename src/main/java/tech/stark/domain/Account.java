package tech.stark.domain;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String account_created;
    private String account_updated;
}
