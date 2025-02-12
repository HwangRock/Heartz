package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review", schema = "test")
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private Client user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "designer_id", nullable = false, referencedColumnName = "designer_id")
    private Designer designer;

    @Lob
    @Column(name = "photoId")
    private String photoId;

    @Column(name = "review_cn", nullable = false)
    private String reviewCn;

    @ColumnDefault("3")
    @Column(name = "star_rating", nullable = false)
    private Integer starRating;

    @Column(name = "regist_date")
    private LocalDate registDate;

}