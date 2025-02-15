package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.PaymentEntity;
import blaybus.mvp.back.dto.Payment.PaymentRequestDTO;
import blaybus.mvp.back.dto.Payment.PaymentResponseDTO;
import blaybus.mvp.back.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository,@Value("${iamport.rest-api-key}") String apiKey,
                          @Value("${iamport.rest-api-secret}") String apiSecret) {
        // 포트원 토큰 발급을 위해 API 키 입력
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponseDTO verifyPayment(PaymentRequestDTO dto) throws IamportResponseException, IOException {
        String imp_uid=dto.getImpuid();
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid); // 결제 검증 시작
        Long amount = (iamportResponse.getResponse().getAmount()).longValue(); // 결제 금액
        String status = iamportResponse.getResponse().getStatus(); // paid 이면 1
        String date= String.valueOf(iamportResponse.getResponse().getPaidAt()); // 날짜를 문자열로 바꿔서 저장

        PaymentResponseDTO paymentDto = PaymentResponseDTO.builder() // Dto 변환
                .impuid(imp_uid)
                .amount(amount)
                .status(status)
                .transactiondate(date)
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

    public List<PaymentResponseDTO> getPaymentsByEmail(String userEmail) {
        // 이메일로 결제 기록 조회
        List<PaymentEntity> paymentEntities = paymentRepository.findAllByUserEmail(userEmail);

        // 조회된 결제 기록을 DTO로 변환하여 반환
        return paymentEntities.stream()
                .map(payment -> PaymentResponseDTO.builder()
                        .impuid(payment.getImpuid())
                        .amount(payment.getAmount())
                        .status(payment.getPaymentStatus())
                        .transactiondate(payment.getCreateAt())
                        .build())
                .collect(Collectors.toList());
    }

    public PaymentResponseDTO cancelPayment(PaymentRequestDTO dto) throws IamportResponseException, IOException {
        String impUid = dto.getImpuid(); // 거래 고유 ID
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData); // Iamport 결제 취소 API 호출

        // Iamport 응답에서 취소 상태 확인
        String status = iamportResponse.getResponse().getStatus(); // 결제 상태
        Long amount = (iamportResponse.getResponse().getAmount()).longValue(); // 취소된 금액
        String date = String.valueOf(iamportResponse.getResponse().getCancelledAt()); // 취소된 시간 문자열로 변환

        // 취소된 거래 정보를 DTO로 생성
        PaymentResponseDTO paymentDto = PaymentResponseDTO.builder()
                .impuid(impUid)
                .amount(amount)
                .status(status.equals("cancelled") ? "cancelled" : "error") // 상태가 'cancelled'이면 "취소됨"
                .transactiondate(date)
                .build();

        // DB에서 거래 ID로 결제 정보 검색
        PaymentEntity paymentEntity = paymentRepository.findByImpuid(impUid);

        if (paymentEntity != null) {
            // 거래 상태를 취소로 업데이트
            if (status.equals("cancelled")) {
                paymentEntity.setPaymentStatus("cancelled"); // 상태 업데이트
                paymentRepository.save(paymentEntity); // 업데이트 저장
                return paymentDto; // 성공적으로 취소된 결과 반환
            }
            else {
                // 결제 취소 실패 시 응답 처리
                paymentDto.setStatus("결제 취소 실패: " + status);
                return paymentDto;
            }
        }
        else {
            // 거래를 찾을 수 없는 경우
            paymentDto.setStatus("거래를 찾을 수 없습니다.");
            return paymentDto;
        }
    }

}
