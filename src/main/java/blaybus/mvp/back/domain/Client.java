package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@RequiredArgsConstructor
@Getter
@DynamicUpdate
@Table(name = "clients", schema = "test")
public class Client {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 100)
    private Role role;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "picture")
    private String picture;

    @Builder
    public Client(String userId, String name, Role role, String email, String picture) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.picture = picture;
    }

    public Client update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}