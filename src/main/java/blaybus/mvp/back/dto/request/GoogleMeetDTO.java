package blaybus.mvp.back.dto.request;

import blaybus.mvp.back.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleMeetDTO {
    private String meetLink; //미트 링크
    private String eventId; //삭제 시 사용할 eventId
    private Reservation reservation;

    @Builder
    public GoogleMeetDTO(String meetLink, String eventId, Reservation reservation) {
        this.meetLink = meetLink;
        this.eventId = eventId;
        this.reservation = reservation;
    }
}
