package blaybus.mvp.back.dto.request;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class ReservationSaveRequestDTO {
    private Client client;
    private Designer designer;
    private LocalDate date;
    private LocalTime time;
    private boolean isOnline;
    private String meetLink;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private String comment;
}
