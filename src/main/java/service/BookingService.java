package service;


import model.Booking;
import model.Client;
import model.ValidationException;
import repository.BookingJDBCRepository;
import repository.ClientJDBCRepository;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class BookingService extends AbstractService<Integer, Booking> {

    private BookingJDBCRepository bookingJDBCRepository;

    private ClientJDBCRepository clientJDBCRepository;

    public BookingService(BookingJDBCRepository bookingJDBCRepository, ClientJDBCRepository clientJDBCRepository) {
        this.bookingJDBCRepository = bookingJDBCRepository;
        this.clientJDBCRepository = clientJDBCRepository;
    }

    public List<Booking> filterByRide(int rideId) {
        return bookingJDBCRepository.findByRide(rideId);
    }

    public List<Pair<String, Integer>> filterByClient(int rideId) {
        List<Pair<String, Integer>> pairList = new ArrayList<>();
        for (Booking booking : filterByRide(rideId)) {
            Client client = clientJDBCRepository.findOne(booking.getClientID());
            String clientName = String.format("%s, %s", client.getFirstName(), client.getLastName());

            pairList.add(new Pair<>(clientName, booking.getSeatNo()));
        }
        return pairList;
    }

    public List<Booking> getAll() {
        return bookingJDBCRepository.findAll();
    }

    @Override
    public void add(Booking booking) {
        try {
            bookingJDBCRepository.save(booking);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getSize() {
        return bookingJDBCRepository.size();
    }
}
