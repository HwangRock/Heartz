package blaybus.mvp.back.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Builder
public class ReservationListResponseDTO {
    private String designerName; //디자이너 이름
    private LocalDate date; //예약 날짜
    private LocalTime time; //예약 시간
    private boolean isOnline; //대면 여부
    private String meetLink;//구글 미트 링크
    private String address;//헤어샵 주소
    private String status;//예약 status
    private Long amount;//가격


}
