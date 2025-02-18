package blaybus.mvp.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ReservationListResponseDTO {
    private Long reservationId; //0. 예약 id(예약 고유 번호)
    private String designerName; //1. 디자이너 이름
    private LocalDate date; //2. 예약 날짜
    private LocalTime time; //3. 예약 시간
    private boolean isOnline; //4. 대면 여부
    private String status;//5. 예약 status
    private String meetLink;//6. 구글 미트 링크
    private String address;//7. 헤어샵 주소
    private Long amount;//8. 가격
}
