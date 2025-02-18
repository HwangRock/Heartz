package blaybus.mvp.back.controller;

import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.dto.response.ReservationListResponseDTO;
import blaybus.mvp.back.service.ClientService;
import blaybus.mvp.back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClientService clientService;

    //1. 사용자 예약 목록 조회
    @GetMapping("/readReservation")
    public List<ReservationListResponseDTO> readDesignerTimeSecdule(){

        String email = clientService.getCurrentUserEmail();
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
            ReservationListResponseDTO.ReservationListResponseDTOBuilder responseBuilder = ReservationListResponseDTO.builder()
                    .designerName(reservation.getDesigner().getName())
                    .date(reservation.getDate())
                    .time(reservation.getTime())
                    .isOnline(reservation.getIsOnline())
                    .status(reservation.getStatus().toString());

            // isOnline에 따라 meetLink 또는 address 설정
            if (reservation.getIsOnline()) {
                responseBuilder.meetLink(reservation.getMeetLink());
            } else {
                responseBuilder.address(reservation.getDesigner().getAddress());
            }

            //가격 정보 삽입
            //paymentservice 필요.
            //param: reservationId, return: amount

            ReservationListResponseDTO response = responseBuilder.build();
        }


        return null;

    }

    //2. 예약 생성
    @PostMapping("/createReservation")
    public ReservationListResponseDTO readDesignerTimeSecdule(@RequestBody SecduleReadRequestDTO secduleReadRequestDTO){

        String email = clientService.getCurrentUserEmail();

        Long userId = clientService.userIdByEmail(email);

        //스케줄 조회
        //return secduleService.readDesignerTimeSecdule(secduleReadRequestDTO, userId);

        //예약 생성 후 결제로 forward 또는 redirect

        return null;
    }

    //3. 예약 취소





}
