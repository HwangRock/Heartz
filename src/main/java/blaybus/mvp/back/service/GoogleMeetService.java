package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.GoogleMeetEntity;
import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.dto.request.GoogleMeetDTO;
import blaybus.mvp.back.repository.GoogleMeetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleMeetService {

    private final GoogleMeetRepository googleMeetRepository;
    private final GoogleCalendarService googleCalendarService;

    //구글 미트 링크 생성
    public GoogleMeetDTO createMeet(String email, LocalDate date, LocalTime time) throws Exception {
        LocalDateTime localStartTime = date.atTime(time);
        LocalDateTime localEndTime = date.atTime(time).plusMinutes(30);
        //meet 생성
        GoogleMeetDTO googleMeetDTO = googleCalendarService.createGoogleMeet(email, localStartTime, localEndTime);
        return googleMeetDTO;
    }

    @Transactional
    public void saveMeet(GoogleMeetDTO googleMeetDTO){
        //GoogleMeetEntity에 저장
        GoogleMeetEntity googleMeetEntity =GoogleMeetEntity.builder()
                .eventId(googleMeetDTO.getEventId())
                .reservation(googleMeetDTO.getReservation())
                .build();
        googleMeetRepository.save(googleMeetEntity);
    }

    @Transactional
    public void deleteMeet(String email, Long reservationId) throws GeneralSecurityException, IOException {
        String eventId = googleMeetRepository.findEventIdByReservationId(reservationId);
        googleCalendarService.deleteByEventId(email, eventId);
        googleMeetRepository.deleteByReservationId(reservationId);
    }

}
