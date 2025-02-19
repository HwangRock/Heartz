package blaybus.mvp.back.controller;

import blaybus.mvp.back.dto.request.PaymentRequestDTO;
import blaybus.mvp.back.dto.response.PaymentResponseDTO;
import blaybus.mvp.back.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/pay")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ResponseBody
    @PostMapping("/portone")
    public PaymentResponseDTO verifyPayment(@RequestBody PaymentRequestDTO dto) throws IamportResponseException, IOException {
        return paymentService.verifyPayment(dto); // 결제 검증 및 DB 값 삽입
    }

    @GetMapping("/records")
    public List<PaymentResponseDTO> getPaymentsByEmail(@RequestBody PaymentRequestDTO dto) {
        return paymentService.getPaymentsByEmail(dto.getEmail());
    }

    @PostMapping("/cancel")
    public PaymentResponseDTO cancelPayment(@RequestBody PaymentRequestDTO dto) throws IamportResponseException, IOException {
        return paymentService.cancelPayment(dto);
    }

    @PostMapping("/virtual")
    public PaymentResponseDTO createVirtualAccount(@RequestBody PaymentRequestDTO dto) {
        return paymentService.createVirtualAccount(dto);
    }


}
