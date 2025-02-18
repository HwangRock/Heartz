package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.*;
import blaybus.mvp.back.dto.request.ReservationRequestDTO;
import blaybus.mvp.back.dto.request.ReservationSaveRequestDTO;
import blaybus.mvp.back.dto.request.SecduleSaveRequestDTO;
import blaybus.mvp.back.event.ReservationEvent;
import blaybus.mvp.back.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    //1. 예약 조회
    /*
        param: userId
     */
    public List<Reservation> readReservation(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return reservations;
    }

    //2. 예약 생성
    @Transactional
    @jakarta.transaction.Transactional
    public Reservation createReservation(ReservationRequestDTO reservationRequestDTO, Designer designer, Client client){

        ReservationSaveRequestDTO reservationSaveRequestDTO = ReservationSaveRequestDTO.builder()
                .client(client)
                .designer(designer)
                .date(reservationRequestDTO.getDate())
                .time(reservationRequestDTO.getTime())
                .isOnline(reservationRequestDTO.isOnline())
                .status(ReservationStatus.READY)
                .createdAt((reservationRequestDTO.getCreatedAt()))
                .build();

        //대면일 경우 구글 미트 링크 생성 및 dto에 정보 저장 --> 결제 후 수행할 로직
        if(reservationRequestDTO.isOnline()){

        }
        //dto->entity
        Reservation reservation = new Reservation(reservationSaveRequestDTO);

        //정보 save
        reservationRepository.save(reservation);

        //예약 정보 생성 이벤트 push -> 이벤트 리스너에서 처리(비동기)
        ReservationEvent event = new ReservationEvent(this, reservation);
        applicationEventPublisher.publishEvent(event);

        //return entity
        return reservation;
    }


    //3. 예약 취소




    public Reservation getReservationById(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElse(null);
    }

    //구글 미트 링크 생성
    private void createMeet() throws Exception {

    }

    public boolean isValidReservation(ReservationRequestDTO reservationRequestDTO){
        //사용자가 예약하기 버튼 누른 시간을 프론트에서 받아서 가져오기. Createat이 사용자가 선택한 시간(time)보다 빠를 때 db에 저장. 만약 더 늦으면 http 응답으로 실패 전송
        // date와 time을 합쳐서 LocalDateTime 객체 생성
        LocalDateTime combinedDateTime = reservationRequestDTO.getDate().atTime(reservationRequestDTO.getTime());

        // createAt(예약 생성 시간)이 combinedDateTime(예약 원하는 시간)보다  늦으면 fail return
        if(!reservationRequestDTO.getCreatedAt().isBefore(combinedDateTime)){
            return false;
        }
        return true;
    }

}
