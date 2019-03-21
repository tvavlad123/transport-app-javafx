package repository;

import model.Client;

import model.ClientValidator;
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

public class ClientJDBCRepository implements IClientRepository {

    private DBUtils dbUtils;

    private static final Logger LOGGER = LogManager.getLogger();

    private static Validator<Client> clientValidator;

    public ClientJDBCRepository(DBUtils dbUtils) {
        LOGGER.info("Initializing ClientJDBCRepository");
        this.dbUtils = dbUtils;
        clientValidator = new ClientValidator();
    }

    @Override
    public int size() {
        LOGGER.traceEntry();
        try {
            Connection connection = dbUtils.getConnection();
            String selectQuery = "select count(*) as SIZE from clients";
            return RepositoryUtils.getTableSize(connection, selectQuery);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Client entity) throws ValidationException {
        LOGGER.traceEntry("Save client {}", entity);
        clientValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String insertQuery = "insert into clients values(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, entity.getClientID());
                preparedStatement.setString(2, entity.getFirstName());
                preparedStatement.setString(3, entity.getLastName());
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " row(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Client entity) throws ValidationException {
        LOGGER.traceEntry("Update client {}", entity);
        clientValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String updateQuery = "update clients set first_name=?, last_name=? where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, entity.getFirstName());
                preparedStatement.setString(2, entity.getLastName());
                preparedStatement.setInt(3, entity.getClientID());
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
        LOGGER.traceEntry("Deleting client with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            RepositoryUtils.deleteEntity(connection, "clients", integer);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public Client findOne(Integer integer) {
        LOGGER.traceEntry("Finding client with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            String findByIDQuery = "select * from clients where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByIDQuery)) {
                preparedStatement.setInt(1, integer);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        Client client = new Client(id, firstName, lastName);
                        LOGGER.traceExit();
                        return client;
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
    public List<Client> findAll() {
        LOGGER.traceEntry();
        List<Client> clientList = new ArrayList<>();
        try {
            Connection connection = dbUtils.getConnection();
            String findAllQuery = "select * from clients";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        Client client = new Client(id, firstName, lastName);
                        clientList.add(client);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        LOGGER.traceExit(clientList);
        return clientList;
    }

    public Client findByFirstAndLastName(String firstName, String lastName) {
        LOGGER.traceEntry();
        Client client = null;
        try {
            Connection connection = dbUtils.getConnection();
            String findByName = "Select * from clients where first_name=? and last_name=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByName)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                         client = new Client(id, firstName, lastName);
                         return client;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return client;
    }
}
