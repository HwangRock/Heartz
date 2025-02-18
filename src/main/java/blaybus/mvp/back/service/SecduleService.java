package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.ReservationStatus;
import blaybus.mvp.back.domain.Secdule;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.SecduleResponseDTO;
import blaybus.mvp.back.repository.ReservationRepository;
import blaybus.mvp.back.repository.SecduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecduleService {

    @Autowired
    private final SecduleRepository secduleRepository;
    @Autowired
    private final ReservationRepository reservationRepository;

    //1. 스케줄 테이블에서 디자이너 타임 테이블 조회
    //1-1. 선택한 날짜 중 예약 가능한 시간대 list
    public SecduleResponseDTO readDesignerTimeSecdule(Long designerId, LocalDate date){

        //타임테이블 map init
        Map<LocalTime, Boolean> availabilityMap = new HashMap<>();
        LocalTime startTime = LocalTime.of(10, 0);
        for (int i = 0; i < 20; i++) {
            LocalTime currentTime = startTime.plusMinutes(i * 30);
            availabilityMap.put(currentTime, true);
        }

        //date가 오늘이면 시간 비교해야함.
        LocalDate today = LocalDate.now();
        if (date.isEqual(today)) {
            // 현재 시간 이전의 시간대는 예약 불가능하도록 false로 설정
            LocalTime now = LocalTime.now();
            for (LocalTime time : availabilityMap.keySet()) {
                if (time.isBefore(now)) {
                    availabilityMap.put(time, false);
                }
            }
        }

        //이미 예약 완료된 시간 정보 가져오기
        List<Secdule> reservedTimes = secduleRepository.findByDesignerIdAndDate(designerId, date);

        //이미 예약이 완료된 시간 타임테이블에서 제외.
        for (Secdule secduleObj : reservedTimes) {
            LocalTime reservedTime = secduleObj.getStartTime();
            if (availabilityMap.containsKey(reservedTime)) {
                availabilityMap.put(reservedTime, false);
            }
        }

        List<Map.Entry<LocalTime, Boolean>> entryList = new ArrayList<>(availabilityMap.entrySet());
        entryList.sort(Map.Entry.comparingByKey());

        //secduleResponseDTO에 타임테이블 값 저장.
        SecduleResponseDTO secduleResponseDTO = new SecduleResponseDTO(entryList);

        return secduleResponseDTO;

    }

    //2. 스케줄 테이블 데이터 생성
    //2-1. 예약 시 스케줄 테이블에 해당 정보 저장
    public Secdule saveDesignerTimeSecdule(Reservation reservation, Designer designer){
        Secdule secdule = Secdule.builder()
                .designer(designer)
                .reservation(reservation)
                .date(reservation.getDate())
                .startTime(reservation.getTime())
                .endTime(reservation.getTime().plusMinutes(30))
                .build();

        return secduleRepository.save(secdule);
    }


    //3. 스케줄 테이블 데이터 삭제
    //3-1. 예약 취소 시 스케줄 테이블 데이터 삭제
    //3-2. 결제 취소 시 스케줄 테이블 데이터 삭제(계좌 이체 기간 만료 포함)
    /*
        param: 예약 id
     */
    public void removeDesignerTimeSecdule(Long reservationId){
        secduleRepository.deleteByReservationId(reservationId);
    }

    @Transactional
    public void goBackPage(Long userId, LocalDate date, Designer designer){

        //사용자의 대기 중인 예약이 있는지 확인 해당 날짜. 해당 디자이너.
        List<Reservation> ready_reservationsByUser = reservationRepository.findByUserIdAndStatusAndDateAndDesigner(userId, ReservationStatus.READY, date, designer);

        if(!ready_reservationsByUser.isEmpty()) {
            for (Reservation ready_reservation : ready_reservationsByUser) {
                // 스케줄 테이블에서 해당 예약 삭제.(param: 예약 id)
                secduleRepository.deleteByReservationId(ready_reservation.getId());
                // 예약 테이블에서 해당 예약 삭제.(param: 예약 id)
                reservationRepository.deleteById(ready_reservation.getId());
            }
        }
    }

}
