package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.PaymentEntity;
import blaybus.mvp.back.dto.Payment.PaymentRequestDTO;
import blaybus.mvp.back.dto.Payment.PaymentResponseDTO;
import blaybus.mvp.back.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        // 포트원 토큰 발급을 위해 API 키 입력
        this.iamportClient = new IamportClient("REST_API_KEY",
                "REST_API_SECRET");
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponseDTO verifyPayment(PaymentRequestDTO dto) throws IamportResponseException, IOException {
        String imp_uid=dto.getImpuid();
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid); // 결제 검증 시작
        Long amount = (iamportResponse.getResponse().getAmount()).longValue(); // 결제 금액
        String status = iamportResponse.getResponse().getStatus(); // paid 이면 1

        PaymentResponseDTO paymentDto = PaymentResponseDTO.builder() // Dto 변환
                .impuid(imp_uid)
                .amount(amount)
                .status(status)
                .build();

        if (paymentRepository.countByImpuidContainsIgnoreCase(imp_uid) == 0) { // 중복하는 값이 없으면
            if (iamportResponse.getResponse().getStatus().equals("paid")) { // 결제가 정상적으로 이루어졌으면
                PaymentEntity paymentEntity = new PaymentEntity(dto); // Dto -> Entity 변환
                paymentRepository.save(paymentEntity); // 변환된 Entity DB 저장
                return paymentDto; // 클라이언트에게 Dto 값 JSON 형태 반환
            }
            else {
                paymentDto.setStatus("결제 오류입니다. 다시 시도해주세요."); // 클라이언트에게 Status 값 오류 코드 보냄
                return paymentDto;
            }
        }
        else {
            paymentDto.setStatus("이미 결제 되었습니다."); // 클라이언트에게 Status 값 오류 코드 보냄
            return paymentDto;
        }
    }
}
