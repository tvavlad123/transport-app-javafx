package controller;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import model.Booking;
import model.Client;
import model.Ride;
import model.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.*;
import service.ClientService;
import service.RideService;
import utils.ErrorAlert;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Bookings extends AbstractController<Integer, Booking> {
    private static final Logger LOGGER = LogManager.getLogger();
    public Label rideDetails;
    public Spinner<Integer> spinner;
    public TextField firstName;
    public TextField lastName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private Ride trip;
    private ClientService clientService;

    public void setRide(Ride ride, RideService rideService) {
        this.trip = ride;
        rideDetails.setText(ride.getDestination() + "\n" +
                ride.getDate() + "\n" +
                ride.getHour() + "\n" +
                rideService.availableSeatsRide(ride) + "\n");
        spinner.setEditable(true);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
                RideService.getMaxAvailablePlaces(), 1));
    }

    public void book() {
        LOGGER.traceEntry("Booking ride");
        try {
            DBUtils dbUtils = new DBUtils("db.properties");
            clientService = new ClientService(new ClientJDBCRepository(dbUtils));
            clientService.add(new Client(clientService.getSize() + 1, firstName.getText(), lastName.getText()));
            Random random = new Random();
            boolean correct = true;
            int rInt = 0;
            while (correct) {
                rInt = random.nextInt(19);
                for (Booking booking : this.service.getAll()) {
                    if (rInt == booking.getSeatNo()) {
                        break;
                    }
                }
                correct = false;
            }
            this.service.add(new Booking(this.service.getSize() + 1,
                    clientService.filterByClientId(firstName.getText(), lastName.getText()), trip.getRideID(), rInt));
            System.out.println(trip.getRideID());
            spinner.getScene().getWindow().getOnCloseRequest().handle(null);
            LOGGER.traceExit("Ride booked");
        } catch (ValidationException e) {
            LOGGER.error(e);
            ErrorAlert.showError(e.getMessage());
            e.printStackTrace();
        }

    }
}
