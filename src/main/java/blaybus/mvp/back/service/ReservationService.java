package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.Secdule;
import blaybus.mvp.back.dto.request.SecduleSaveRequestDTO;
import blaybus.mvp.back.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;


    //1. 예약 조회
    /*
        param: userId
     */
    public List<Reservation> readReservation(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations;
    }
    
    //2. 예약 생성
    // 만약에 11시 시간 거를 10:59분에 예약하려고해서 예약하기 눌렀는데 시간이 지난 경우 테이블에 정보 삽입하면 안됨
    // 예약 안 된다~~ 처리
    
    //3. 예약 취소

    


    public Reservation getReservationById(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElse(null);
    }

    //구글 미트 링크 생성
    private void createMeet() throws Exception {

    }

}
