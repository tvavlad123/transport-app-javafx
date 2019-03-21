package repository;

import model.Ride;
import model.RideValidator;
import model.ValidationException;
import model.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RideJDBCRepository implements IRideRepository {

    private DBUtils dbUtils;

    private static final Logger LOGGER = LogManager.getLogger();

    private static Validator<Ride> rideValidator;

    public RideJDBCRepository(DBUtils dbUtils) {
        LOGGER.info("Initializing RideJDBCRepository");
        this.dbUtils = dbUtils;
        rideValidator = new RideValidator();
    }

    @Override
    public int size() {
        LOGGER.traceEntry();
        try {
            Connection connection = dbUtils.getConnection();
            String selectQuery = "select count(*) as SIZE from rides";
            return RepositoryUtils.getTableSize(connection, selectQuery);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Ride entity) throws ValidationException {
        LOGGER.traceEntry("Save ride {}", entity);
        rideValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String insertQuery = "insert into rides values(?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, entity.getRideID());
                preparedStatement.setString(2, entity.getDestination());
                preparedStatement.setDate(3, Date.valueOf(entity.getDate()));
                preparedStatement.setString(4, entity.getHour());
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " row(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Ride entity) throws ValidationException {
        LOGGER.traceEntry("Update ride {}", entity);
        rideValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String updateQuery = "update rides set destination=?, date=?, hour=? where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, entity.getDestination());
                preparedStatement.setString(2, entity.getDate());
                preparedStatement.setString(3, entity.getHour());
                preparedStatement.setInt(4, entity.getRideID());
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
        LOGGER.traceEntry("Deleting ride with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            RepositoryUtils.deleteEntity(connection, "rides", integer);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public Ride findOne(Integer integer) {
        LOGGER.traceEntry("Finding ride with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            String findByIDQuery = "select * from rides where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByIDQuery)) {
                preparedStatement.setInt(1, integer);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String destination = resultSet.getString("destination");
                        String date = resultSet.getString("date");
                        String hour = resultSet.getString("hour");
                        Ride ride = new Ride(id, destination, date, hour);
                        LOGGER.traceExit();
                        return ride;
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
    public List<Ride> findAll() {
        LOGGER.traceEntry();
        List<Ride> rideList = new ArrayList<>();
        try {
            Connection connection = dbUtils.getConnection();
            String findAllQuery = "select * from rides";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String destination = resultSet.getString("destination");
                        String date = resultSet.getString("date");
                        String hour = resultSet.getString("hour");
                        Ride ride = new Ride(id, destination, date, hour);
                        rideList.add(ride);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        LOGGER.traceExit(rideList);
        return rideList;
    }

    public Pair<Ride, Integer> findRide(String destination, String date, String hour) {
        LOGGER.traceEntry();
        Ride ride = null;
        int countRows = 0;
        Pair<Ride, Integer> pair = null;
        try {
            Connection connection = dbUtils.getConnection();
            String findRideQuery = "select * from rides where destination=? and date=? and hour=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findRideQuery)) {
                preparedStatement.setString(1, destination);
                preparedStatement.setString(2, date);
                preparedStatement.setString(3, hour);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        ride = new Ride(id, destination, date, hour);
                    }
                }
            }
            String countPlaces = "select count(bookings.id) from bookings inner join rides on rides.id=bookings.id_ride " +
                    "where rides.id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(countPlaces)) {
                preparedStatement.setInt(1, ride.getRideID());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        countRows = resultSet.getInt(1);
                    }
                }
            }
            pair = new Pair<>(ride, countRows);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return pair;
    }
}
