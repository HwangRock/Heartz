package blaybus.mvp.back.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SecduleReadRequestDTO {

    private Long designerId;    //선택한 디자이너 id
    //디자이너 이름이 아니라 디자이너 id로 받아야 식별 가능.
    //디자이너 이름으로 디자이너 id 찾는 쿼리를 넣으려면 디자이너 상세 정보 전체가 있어야 함.
    private LocalDate date;     //선택한 날짜

}
