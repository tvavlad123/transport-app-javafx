package repository;

import model.Booking;
import model.BookingValidator;
import model.ValidationException;
import model.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingJDBCRepository implements IBookingRepository {

    private DBUtils dbUtils;

    private static final Logger LOGGER = LogManager.getLogger();

    private static Validator<Booking> bookingValidator;

    public BookingJDBCRepository(DBUtils dbUtils) {
        LOGGER.info("Initializing BookingJDBCRepository");
        this.dbUtils = dbUtils;
        bookingValidator = new BookingValidator();
    }

    @Override
    public int size() {
        LOGGER.traceEntry();
        try {
            Connection connection = dbUtils.getConnection();
            String selectQuery = "select count(*) as SIZE from bookings";
            return RepositoryUtils.getTableSize(connection, selectQuery);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Booking entity) throws ValidationException {
        LOGGER.traceEntry("Save booking {}", entity);
        bookingValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String insertQuery = "insert into bookings values(?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, entity.getBookingID());
                preparedStatement.setInt(2, entity.getClientID());
                preparedStatement.setInt(3, entity.getRideID());
                preparedStatement.setInt(4, entity.getSeatNo());
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " row(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Booking entity) throws ValidationException {
        LOGGER.traceEntry("Update booking {}", entity);
        bookingValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String updateQuery = "update bookings set id_client=?, id_ride=?, seat_no=? where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, entity.getClientID());
                preparedStatement.setInt(2, entity.getRideID());
                preparedStatement.setInt(3, entity.getSeatNo());
                preparedStatement.setInt(4, entity.getBookingID());
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " rows(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {
        LOGGER.traceEntry("Deleting booking with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            RepositoryUtils.deleteEntity(connection, "bookings", integer);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public Booking findOne(Integer integer) {
        LOGGER.traceEntry("Finding booking with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            String findByIDQuery = "select * from bookings where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByIDQuery)) {
                preparedStatement.setInt(1, integer);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        int idClient = resultSet.getInt("id_client");
                        int idRide = resultSet.getInt("id_ride");
                        int seatNo = resultSet.getInt("seat_no");
                        Booking booking = new Booking(id, idClient, idRide, seatNo);
                        LOGGER.traceExit();
                        return booking;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> findAll() {
        LOGGER.traceEntry();
        List<Booking> bookingList = new ArrayList<>();
        try {
            Connection connection = dbUtils.getConnection();
            String findAllQuery = "select * from bookings";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
                getRidesById(bookingList, preparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        LOGGER.traceExit(bookingList);
        return bookingList;
    }

    public List<Booking> findByRide(int rideId) {
        LOGGER.traceEntry();
        List<Booking> bookingList = new ArrayList<>();
        try {
            Connection connection = dbUtils.getConnection();
            String findByRide = "SELECT * FROM bookings WHERE id_ride = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByRide)) {
                preparedStatement.setInt(1, rideId);
                getRidesById(bookingList, preparedStatement);
            }

        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return bookingList;
    }

    private void getRidesById(List<Booking> bookingList, PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int clientId = resultSet.getInt("id_client");
                int ride = resultSet.getInt("id_ride");
                int seatNo = resultSet.getInt("seat_no");
                Booking booking = new Booking(id, clientId, ride, seatNo);
                bookingList.add(booking);
            }
        }
    }

}
