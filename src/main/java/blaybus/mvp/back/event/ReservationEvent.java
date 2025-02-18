package blaybus.mvp.back.event;

import blaybus.mvp.back.domain.Reservation;
import org.springframework.context.ApplicationEvent;

public class ReservationEvent extends ApplicationEvent {
    private final Reservation reservation;

    public ReservationEvent(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
