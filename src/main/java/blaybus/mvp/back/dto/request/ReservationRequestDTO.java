package blaybus.mvp.back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class ReservationRequestDTO {
    private String userId;
    private Long designerId;
    private LocalDate date;
    private LocalTime time;
    private boolean isOnline;
    private String comment;
}
