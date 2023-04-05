package Controller;

import Data.ReservationData;
import Model.Book;
import Model.Member;
import Model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    private ReservationData reservationData;

    public ReservationController(ReservationData reservationData) {
        this.reservationData = reservationData;
    }

    public void addReservation(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        reservations.add(reservation);
        reservationData.save(reservations);
    }

    public void removeReservation(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        reservations.remove(reservation);
        reservationData.save(reservations);
    }

    public List<Reservation> getReservationsForMember(Member member) {
        return reservationData.getReservationsForMember(member);
    }

    public List<Reservation> getAllReservations() {
        return reservationData.load();
    }

}
