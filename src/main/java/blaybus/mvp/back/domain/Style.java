package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "style", schema = "test")
public class Style {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private Client user;

    @Column(name = "feature")
    private String feature;

    @Column(name = "make_up")
    private String makeUp;

}