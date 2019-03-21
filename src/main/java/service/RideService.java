package service;

import model.Ride;
import repository.IRepository;
import repository.RideJDBCRepository;

import java.util.Comparator;
import java.util.List;

public class RideService extends AbstractService<Integer, Ride> {
    public static final int MAX_AVAILABLE_PLACES = 18;

    private RideJDBCRepository rideJDBCRepository;

    public RideService(IRepository<Integer, Ride> repository) {
        super(repository);
        rideJDBCRepository = (RideJDBCRepository) repository;
    }

    public int availableSeatsRide(Ride ride) {
        int availableSeats = MAX_AVAILABLE_PLACES -
                rideJDBCRepository.findRide(ride.getDestination(), ride.getDate(), ride.getHour()).getSecond();
        if (availableSeats <= 0) return 0;
        else return availableSeats;
    }

    public List<Ride> filterDestinationDateHour(String destination, String date, String hour) {
        return filterAndSorter(this.getAll(),
                x -> x.getDestination().toLowerCase().contains(destination.toLowerCase()) && x.getDate().contains(date)
                        && x.getHour().contains(hour), Comparator.comparing(Ride::getRideID)
        );
    }

    public static int getMaxAvailablePlaces() {
        return MAX_AVAILABLE_PLACES;
    }
}
