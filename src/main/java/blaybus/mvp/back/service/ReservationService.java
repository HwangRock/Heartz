package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.*;
import blaybus.mvp.back.dto.request.*;
import blaybus.mvp.back.dto.response.PaymentResponseDTO;
import blaybus.mvp.back.dto.response.ReservationListResponseDTO;
import blaybus.mvp.back.event.ReservationEvent;
import blaybus.mvp.back.repository.ReservationRepository;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final SecduleService secduleService;
    private final ClientService clientService;
    private final PaymentService paymentService;

    @Autowired
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GoogleMeetService googleMeetService;


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
    public Reservation createReservation(ReservationRequestDTO reservationRequestDTO, Designer designer, Client client) throws Exception {

        ReservationSaveRequestDTO reservationSaveRequestDTO = ReservationSaveRequestDTO.builder()
                .client(client)
                .designer(designer)
                .date(reservationRequestDTO.getDate())
                .time(reservationRequestDTO.getTime())
                .isOnline(reservationRequestDTO.isOnline())
                .status(ReservationStatus.READY)
                .createdAt((reservationRequestDTO.getCreatedAt()))
                .build();

        GoogleMeetDTO googleMeetDTO = null;
        //비대면일 경우 구글 미트 링크 생성 및 dto에 정보 저장
        if(reservationRequestDTO.isOnline()){
            googleMeetDTO = googleMeetService.createMeet(client.getEmail(), reservationRequestDTO.getDate(), reservationRequestDTO.getTime());
            reservationSaveRequestDTO.setMeetLink(googleMeetDTO.getMeetLink());
        }
        //dto->entity
        Reservation reservation = new Reservation(reservationSaveRequestDTO);

        //정보 save
        reservationRepository.save(reservation);

        if(googleMeetDTO != null){
            googleMeetDTO.setReservation(reservation);
            googleMeetService.saveMeet(googleMeetDTO);
        }

        //예약 정보 생성 이벤트 push -> 이벤트 리스너에서 처리(비동기)
        ReservationEvent event = new ReservationEvent(this, reservation);
        applicationEventPublisher.publishEvent(event);

        //return entity
        return reservation;
    }


    //3. 예약 취소
    @Transactional(rollbackFor = Throwable.class)
    public void cancelReservation(String email, Long reservationId) throws IamportResponseException, IOException, GeneralSecurityException {

        //reservation 예약 status 변경(update by reservationId)
        this.changeStatus(reservationId);

        //스케줄 테이블에서 해당 스케줄 삭제(deleteByReservationId) 성공 시 true return?
        secduleService.deleteSecdule(reservationId);

        //해당 reservationId로 Reservation 객체 불러오기.(findById)
        Reservation reservation = this.getReservationById(reservationId);

        //해당 reservationId로 PaymentEntity 객체 불러오기(findByReservationId)
        PaymentEntity paymentEntity = paymentService.findByReservationId(reservationId);

        //불러온 PaymentEntity 및 Reservation으로 PaymentRequestDTO 생성
        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .reservation(reservation)
                .impuid(paymentEntity.getImpuid())
                .build();
        //결제 취소(결제 status 변경)
        PaymentResponseDTO paymentResponseDTO = paymentService.cancelPayment(paymentRequestDTO);

        //구글 미트 링크 삭제
        googleMeetService.deleteMeet(email, reservationId);

    }

    @Transactional
    public void saveComment(Long reservationId, String comment){
        reservationRepository.saveComment(reservationId, comment);
    }

    @Transactional
    public void updateStatus(Long reservationId, ReservationStatus status){
        reservationRepository.updateStatus(reservationId, status);
    }

    
    //예약 상태 변경(complete) - 결제 확인
    //예약 상태 변경(ongoing) - 입금 대기 중

    public Reservation getReservationById(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElse(null);
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

    @Transactional
    public List<ReservationListResponseDTO> convertToResponseList(List<Reservation> reservations){
        List<ReservationListResponseDTO> reservationListResponse = new ArrayList<>();

        for(Reservation reservation: reservations){
            // responseDTO에서 1~5번 정보 set
            ReservationListResponseDTO response = this.convertToResponse(reservation);
            reservationListResponse.add(response);
        }
        return reservationListResponse;
    }

    public ReservationListResponseDTO convertToResponseDetail(Reservation reservation){
        return this.convertToResponse(reservation);
    }

    @Transactional
    private ReservationListResponseDTO convertToResponse(Reservation reservation){
        // responseDTO에서 1~5번 정보 set
        ReservationListResponseDTO.ReservationListResponseDTOBuilder responseBuilder = ReservationListResponseDTO.builder()
                .reservationId(reservation.getId())
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
        return response;
    }

    //예약 상태 변경(canceled) - 취소
    private void changeStatus(Long reservationId){
        reservationRepository.changeStatus(reservationId);
    }

    //구글 미트 링크 생성
    private void createMeet() throws Exception {

    }



}
