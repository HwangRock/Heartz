package blaybus.mvp.back.domain;

public enum ReservationStatus {
    READY,
    ONGOING,    // 예약 진행 중(결제 대기 중)
    COMPLETE,   // 예약 완료(결제 확인)
    PASSED,     // 지난 예약
    CANCELED    // 취소된 예약
}
