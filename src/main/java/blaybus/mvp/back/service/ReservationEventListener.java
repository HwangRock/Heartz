package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.event.ReservationEvent;
import blaybus.mvp.back.repository.ReservationRepository;
import blaybus.mvp.back.repository.SecduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Transactional
public class ReservationEventListener implements ApplicationListener<ReservationEvent> {

    private final ReservationRepository reservationRepository;
    private final SecduleRepository secduleRepository;

    @Autowired
    public ReservationEventListener(ReservationRepository reservationRepository, SecduleRepository secduleRepository){
        this.reservationRepository = reservationRepository;
        this.secduleRepository = secduleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ReservationEvent reservationEvent) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            //reservation의 id 가져옴.
            Reservation reservation = reservationEvent.getReservation();
            Long reservationId = reservation.getId();
            //테이블에서 해당 reservationId의 status 가져옴
            Reservation chkReservation = reservationRepository.findById(reservationId).orElse(null);
            //READY인 경우 delete
            if(chkReservation != null && "READY".equals(chkReservation.getStatus().toString())){
                try{
                    //스케줄 테이블 업데이트
                    secduleRepository.deleteByReservationId(reservationId);
                    //예약 테이블 업데이트
                    reservationRepository.deleteById(reservationId);
                } catch (Exception e){
                    log.error("이벤트 리스너에서 테이블 정보 삭제 도중 오류", e);
                }

            }
        }, 10, TimeUnit.MINUTES);
    }
}
