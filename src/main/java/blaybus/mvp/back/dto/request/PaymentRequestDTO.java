package blaybus.mvp.back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class PaymentRequestDTO {
    private String impuid; // 거래 고유 번호
    private String status; // 결제여부 paid : 1 , 그 외 실패
    private Long amount; // 결제금액
    private String email; // 유저 이메일
    private String name; // 유저 이름
    private String createDate; // 생성 날짜
}
