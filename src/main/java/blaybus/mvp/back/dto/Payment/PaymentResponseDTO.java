package blaybus.mvp.back.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class PaymentResponseDTO {
    private String impuid; // 거래 고유 번호
    private String status; // 결제여부 paid : 1 , 그 외 실패
    private Long amount; // 결제금액
    private String transactiondate;
}
