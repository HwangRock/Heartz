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

    @Builder
    public Style(Client user, String feature, String makeUp) {
        this.user = user;
        this.feature = feature;
        this.makeUp = makeUp;
    }

}