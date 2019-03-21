package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.BookingJDBCRepository;
import repository.ClientJDBCRepository;
import repository.DBUtils;
import service.AbstractService;
import service.BookingService;
import service.RideService;
import utils.ErrorAlert;
import utils.Observer;
import utils.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainWindow extends AbstractController<Integer, Ride> implements Observer<Ride> {
    public Button logOutBtn;
    public TableColumn<Object, Object> Destination;
    public TableColumn<Object, Object> Date;
    public TableColumn<Object, Object> Hour;
    public TableColumn<Object, Object> NoSeats;
    public TableView<RideWrapper> table_all;
    public TableColumn<Object, Object> ClientName;
    public TableColumn<Object, Object> SeatNo;
    public TableView<SearchWrapper> table_search;

    public TextField destination;
    public TextField date;
    public TextField hour;

    private List<Ride> rides = new ArrayList<>();
    List<RideWrapper> rideWrapperList;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.traceEntry("Initialize main window.");
        Destination.setCellValueFactory(new PropertyValueFactory<>("Destination"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Hour.setCellValueFactory(new PropertyValueFactory<>("Hour"));
        NoSeats.setCellValueFactory(new PropertyValueFactory<>("AvailableSeats"));
        ClientName.setCellValueFactory(new PropertyValueFactory<>("ClientName"));
        SeatNo.setCellValueFactory(new PropertyValueFactory<>("SeatNo"));
        LOGGER.traceExit("UI elements initialized");
    }

    public void LogOut() {

        ((Stage) logOutBtn.getScene().getWindow()).close();
        logOutBtn.getScene().getWindow().getOnCloseRequest().handle(null);
    }

    @Override
    public void setService(AbstractService<Integer, Ride> service) {
        LOGGER.traceEntry("Setting ride service");
        ObservableList<RideWrapper> searchRides = FXCollections.observableArrayList();
        super.setService(service);
        RideService rideService = (RideService) this.service;
        rideWrapperList = new ArrayList<>();
        for (Ride ride : this.service.getAll()) {
            rideWrapperList.add(new RideWrapper(ride.getRideID(), ride.getDestination(),
                    ride.getDate(), ride.getHour(), rideService.availableSeatsRide(ride)));
        }
        searchRides.addAll(rideWrapperList);
        table_all.setItems(searchRides);
        LOGGER.traceExit("Table populated.");
    }

    @Override
    public void NotifyOnEvent() {
        ObservableList<RideWrapper> searchRides = FXCollections.observableArrayList();
        RideService rideService = (RideService) this.service;
        rideWrapperList = new ArrayList<>();
        for (Ride ride : rideService.getAll()) {
            rideWrapperList.add(new RideWrapper(ride.getRideID(), ride.getDestination(),
                    ride.getDate(), ride.getHour(), rideService.availableSeatsRide(ride)));
        }
        searchRides.addAll(rideWrapperList);
        table_all.setItems(searchRides);
    }

    public void book() {
        Stage stage = (Stage) table_search.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Booking.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Bookings controller = fxmlLoader.getController();
            DBUtils dbUtils = new DBUtils("db.properties");
            controller.setService(new BookingService(new BookingJDBCRepository(dbUtils), new ClientJDBCRepository(dbUtils)));
            controller.setRide(rides.get(0), (RideService) this.service);
            Stage stage1 = new Stage();
            stage1.setTitle("Transport");
            stage1.setScene(new Scene(root));
            stage1.show();
            stage1.setOnCloseRequest(event -> {
                search();
                stage.show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search() {
        Validator<Ride> rideValidator = new RideValidator();
        LOGGER.traceEntry("Searching for ride");
        table_search.getItems().clear();
        DBUtils dbUtils = new DBUtils("db.properties");
        List<Integer> seats = new ArrayList<>();
        BookingService bookingService = new BookingService(new BookingJDBCRepository(dbUtils), new ClientJDBCRepository(dbUtils));
        rides.removeAll(rides);

        rides.addAll(((RideService) service).filterDestinationDateHour(destination.getText(), date.getText(), hour.getText()));

        if (rides.isEmpty()) {
            ErrorAlert.showError("Invalid ride!");
            return;
        }
        if (destination.getText().isEmpty() || date.getText().isEmpty() || hour.getText().isEmpty()) {
            ErrorAlert.showError("Please search for a ride");
            return;
        }
        else {
            ObservableList<SearchWrapper> search_model = FXCollections.observableArrayList();
            List<Pair<String, Integer>> pairList = (bookingService.filterByClient(rides.get(0).getRideID()));
            List<SearchWrapper> searchWrapperList = new ArrayList<>();
            for (Pair<String, Integer> pair : pairList) {
                searchWrapperList.add(new SearchWrapper(pair.getFirst(), pair.getSecond()));
                seats.add(pair.getSecond());
            }
            for (int index = 1; index <= RideService.getMaxAvailablePlaces(); index++) {
                if (seats.indexOf(index) == -1) {
                    searchWrapperList.add(new SearchWrapper("-", index));
                }
            }
            Collections.sort(searchWrapperList, (o1, o2) -> o1.getSeatNo() - o2.getSeatNo());
            search_model.addAll(searchWrapperList);
            table_search.setItems(search_model);
            LOGGER.traceExit("Display rides");
        }
    }
}
