package blaybus.mvp.back.controller;

import blaybus.mvp.back.domain.*;
import blaybus.mvp.back.dto.request.PaymentRequestDTO;
import blaybus.mvp.back.dto.request.ReservationRequestDTO;
import blaybus.mvp.back.dto.request.ReservationSaveRequestDTO;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.dto.response.PaymentResponseDTO;
import blaybus.mvp.back.dto.response.ReservationListResponseDTO;
import blaybus.mvp.back.event.ReservationEvent;
import blaybus.mvp.back.service.*;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final SecduleService secduleService;
    private final ClientService clientService;
    private final PaymentService paymentService;
    private final DesignerService designerService;

    //1. 사용자 예약 목록 조회
    @GetMapping("/readReservation")
    public List<ReservationListResponseDTO> readDesignerTimeSecdule(){

        String email = clientService.getCurrentUserEmail();
        //String email = "choeunbin0324@gmail.com";
        Long userId = clientService.userIdByEmail(email);

        //예약 조회
        List<Reservation> reservations = reservationService.readReservation(userId);

        if(reservations.isEmpty()){
            //어떻게 return할지 고민
            return null;
        }
        //List<Reservation> -> List<ReservationListResponseDTO> 쿼리
        List<ReservationListResponseDTO> reservationListResponse = reservationService.convertToResponseList(reservations);
        return reservationListResponse;
    }

    //2. 예약 생성
    @PostMapping("/createReservation")
    public ResponseEntity<Object> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){

        //response body에 담을 Map 객체 생성
        Map<String, Object> response = new HashMap<>();
        //String email = clientService.getCurrentUserEmail();
        String email = "choeunbin0324@gmail.com";
        String name = clientService.nameByEmail(email);
        Long userId = clientService.userIdByEmail(email);
        reservationRequestDTO.setUserId(userId);

        //사용자가 요청한 예약 시간이 유효한지 확인
        if(!reservationService.isValidReservation(reservationRequestDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 예약 시간입니다.");
        }

        //예약 정보 생성
        Designer designer = designerService.getDesignerById(reservationRequestDTO.getDesignerId());
        Client client = clientService.clientByEmail(email);
        Reservation reservation = reservationService.createReservation(reservationRequestDTO, designer, client);

        //스케줄 테이블 업데이트
        secduleService.saveDesignerTimeSecdule(reservation, designer);

        //사용자 정보 map담기
        response.put("name", name);
        response.put("email", email);
        //생성된 예약 정보 map담기
        response.put("reservation", reservation);

        return ResponseEntity.ok(response);
    }

    //3. 예약 취소
    /*
        Transaction 관리를 위해 reservation service에 포함
        param: reservationId
        return: httpResponse 객체.
            성공 시: 200
     */
    @GetMapping("/deleteReservation")
    public ResponseEntity<Void> deleteReservation(@RequestParam Long reservationId) throws IamportResponseException, IOException {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    //4. 예약 완료 페이지.(결제 완료 후 여기로 요청)
    @GetMapping("/readReservationDetail")
    public ResponseEntity<Object> readReservationDetail(@RequestParam Long reservationId){
        Map<String, Object> response = new HashMap<>();

        //String email = clientService.getCurrentUserEmail();
        String email = "choeunbin0324@gmail.com";
        String name = clientService.nameByEmail(email);
        Long userId = clientService.userIdByEmail(email);

        //Reservation 객체 가져오기
        Reservation reservation = reservationService.getReservationById(reservationId);

        //가격 정보 불러오기
        Long amount = paymentService.getAmountByReservationId(reservationId);

        //List<Reservation> -> List<ReservationListResponseDTO> 쿼리
        ReservationListResponseDTO reservationDetailResponse = reservationService.convertToResponseDetail(reservation);
        reservationDetailResponse.setAmount(amount);

        //사용자 정보 map담기
        response.put("name", name);
        response.put("email", email);
        //예약 정보 map담기
        response.put("reservationDetail", reservationDetailResponse);
        return ResponseEntity.ok(response);
    }

    //5. 예약 정보 추가
    @PostMapping("/reservationadditonal")
    public ResponseEntity<Void> reservationadditonal(@RequestBody Map<String, Object> requestBody){
        Long reservationId = ((Integer)requestBody.get("reservationId")).longValue();
        String comment = (String)requestBody.get("comment");
        ReservationStatus status = ReservationStatus.valueOf((String)requestBody.get("status"));

        reservationService.saveComment(reservationId, comment);
        reservationService.updateStatus(reservationId, status);

        return ResponseEntity.ok().build();
    }






}
