package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.Secdule;
import blaybus.mvp.back.dto.request.SecduleReadRequestDTO;
import blaybus.mvp.back.dto.response.SecduleResponseDTO;
import blaybus.mvp.back.repository.SecduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SecduleService {

    @Autowired
    private final SecduleRepository secduleRepository;

    //1. 스케줄 테이블에서 디자이너 타임 테이블 조회
    //1-1. 선택한 날짜 중 예약 가능한 시간대 list
    public SecduleResponseDTO readDesignerTimeSecdule(SecduleReadRequestDTO secduleReadRequestDTO){

        Long designerId = secduleReadRequestDTO.getDesignerId();
        LocalDate date = secduleReadRequestDTO.getDate();

        //타임테이블 map init
        Map<LocalTime, Boolean> availabilityMap = new HashMap<>();
        LocalTime startTime = LocalTime.of(10, 0);
        for (int i = 0; i < 21; i++) {
            LocalTime currentTime = startTime.plusMinutes(i * 30);
            availabilityMap.put(currentTime, true);
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

        //secduleResponseDTO에 타임테이블 값 저장.
        SecduleResponseDTO secduleResponseDTO = new SecduleResponseDTO(availabilityMap);

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

        //이미 예약 완료된 시간 정보 가져오기
        return secduleRepository.save(secdule);
    }


    //3. 스케줄 테이블 데이터 삭제
    //3-1. 예약 취소 시 스케줄 테이블 데이터 삭제
    //3-2. 결제 취소 시 스케줄 테이블 데이터 삭제(계좌 이체 기간 만료 포함)
    /*
        param: 예약 id
     */
    public void removeDesignerTimeSecdule(Long reservationId){
        secduleRepository.deleteById(reservationId);
    }

}
