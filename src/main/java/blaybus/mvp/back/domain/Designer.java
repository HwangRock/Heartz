package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@RequiredArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "designers", schema = "test")
public class Designer {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "text")
    private String text;

    @Builder(toBuilder = true)
    public Designer(Long designerId, String name, String location, String address, String profilePhoto, String field, Integer offPrice, Integer onPrice, Integer rating, String text) {
        this.designerId = designerId;
        this.name = name;
        this.location = location;
        this.address = address;
        this.profilePhoto = profilePhoto;
        this.field = field;
        this.offPrice = offPrice;
        this.onPrice = onPrice;
        this.rating = rating;
        this.text = text;
    }
}