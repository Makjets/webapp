package tech.stark.webapp.models;

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
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String account_created;
    private String account_updated;
}
