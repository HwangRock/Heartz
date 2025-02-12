package blaybus.mvp.back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "clients", schema = "test")
public class Client {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @ColumnDefault("'0'")
    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "address")
    private String address;

    @Column(name = "email", nullable = false)
    private String email;

}