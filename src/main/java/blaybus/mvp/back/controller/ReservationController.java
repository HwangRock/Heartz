package blaybus.mvp.back.controller;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.ReservationStatus;
import blaybus.mvp.back.dto.request.ReservationRequestDTO;
import blaybus.mvp.back.dto.request.ReservationSaveRequestDTO;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.dto.response.ReservationListResponseDTO;
import blaybus.mvp.back.event.ReservationEvent;
import blaybus.mvp.back.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
//    private final ApplicationEventPublisher applicationEventPublisher;

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
        List<ReservationListResponseDTO> reservationListResponse = new ArrayList<>();

        for(Reservation reservation: reservations){

            // responseDTO에서 1~5번 정보 set
            ReservationListResponseDTO.ReservationListResponseDTOBuilder responseBuilder = ReservationListResponseDTO.builder()
                    .designerName(reservation.getDesigner().getName())
                    .date(reservation.getDate())
                    .time(reservation.getTime())
                    .isOnline(reservation.getIsOnline())
                    .status(reservation.getStatus().toString());

            // responseDTO에서 6or7번 정보 set
            // isOnline에 따라 meetLink 또는 address 설정
            if (reservation.getIsOnline()) {
                responseBuilder.meetLink(reservation.getMeetLink());
            } else {
                responseBuilder.address(reservation.getDesigner().getAddress());
            }

            Long reservationId = reservation.getId();
            //가격 정보 불러오기
            Long amount = paymentService.getAmountByReservationId(reservationId);
            //responseDTO에서 8번 정보(마지막) set
            responseBuilder.amount(amount);

            //reservation response 객체 build
            ReservationListResponseDTO response = responseBuilder.build();
            reservationListResponse.add(response);
        }
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
        @Transactional?
        param: reservationId
        {
        reservation 예약 status 변경(update by reservationId) 성공 시 true return?
        스케줄 테이블에서 해당 스케줄 삭제(deleteByReservationId) 성공 시 true return?
        } 예외 처리?
        해당 reservationId로 Reservation 객체 불러오기.(findById)
        해당 reservationId로 PaymentEntity 객체 불러오기(findByReservationId)
        불러온 PaymentEntity 및 Reservation으로 PaymentRequestDTO 생성
        결제 취소(결제 status 변경)로 redirect

        return: httpResponse 객체.
            성공 시: 200
            실패 시: 401? errorMessage: "예약 취소에 실패했습니다"
     */



    //4. 예약 완료 페이지.(결제 완료 후 여기로 redirect)





}
