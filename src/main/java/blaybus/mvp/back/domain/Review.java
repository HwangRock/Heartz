package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@Getter
@DynamicUpdate
@Table(name = "review", schema = "test")
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public Review(Client client, Designer designer, String photoId, String reviewCn, Integer starRating, LocalDate registDate) {
        this.user = client;
        this.designer = designer;
        this.photoId = photoId;
        this.reviewCn = reviewCn;
        this.starRating = starRating;
        this.registDate = registDate;
    }

}