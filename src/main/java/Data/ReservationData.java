package Data;

import Model.Book;
import Model.Member;
import Model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationData {
    private static final String FILE_NAME = "Reservations.ser";

    public void save(List<Reservation> reservations) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            oos.writeObject(reservations);
            oos.close();
            System.out.println("Reservations saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving reservations to file: " + e.getMessage());
        }
    }

    public List<Reservation> load() {
        List<Reservation> reservations = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
            reservations = (List<Reservation>) ois.readObject();
            ois.close();
            System.out.println("Reservations loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved reservations found.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reservations from file: " + e.getMessage());
        }
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        List<Reservation> reservations = load();
        reservations.add(reservation);
        save(reservations);
    }

    public List<Reservation> getReservationsForMember(Member member) {
        List<Reservation> memberReservations = new ArrayList<>();
        List<Reservation> reservations = load();
        for (Reservation r : reservations) {
            if (r.getMember().equals(member)) {
                memberReservations.add(r);
            }
        }
        return memberReservations;
    }

    public void removeReservation(Reservation reservation) {
        List<Reservation> reservations = load();
        reservations.remove(reservation);
        save(reservations);
    }

    public void removeReservation(Book book) {
        List<Reservation> reservations = load();
        List<Reservation> reservationsToRemove = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getBook().equals(book)) {
                reservationsToRemove.add(r);
            }
        }
        reservations.removeAll(reservationsToRemove);
        save(reservations);
    }
}
