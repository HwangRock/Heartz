package blaybus.mvp.back.dto.request;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.Reservation;
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
public class SecduleSaveRequestDTO {
    private Designer designer;
    private Reservation reservation;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
