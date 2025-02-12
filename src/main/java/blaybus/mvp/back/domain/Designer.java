package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "designers", schema = "test")
public class Designer {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "designer_id", nullable = false)
    private Long designerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "field", nullable = false, length = 100)
    private String field;

    @Column(name = "off_price")
    private Integer offPrice;

    @Column(name = "on_price")
    private Integer onPrice;

    @Lob
    @Column(name = "rating")
    private String rating;

    @Column(name = "account")
    private String account;

}