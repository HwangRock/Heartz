package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.Secdule;
import blaybus.mvp.back.dto.request.SecduleSaveRequestDTO;
import blaybus.mvp.back.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;

    public Reservation getReservationById(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElse(null);
    }
}
