package repository;

import model.Employee;

import model.EmployeeValidator;
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


public class EmployeeJDBCRepository implements IEmployeeRepository {

    private DBUtils dbUtils;

    private static final Logger LOGGER = LogManager.getLogger();

    private static Validator<Employee> employeeValidator;

    public EmployeeJDBCRepository(DBUtils dbUtils) {
        LOGGER.info("Initializing EmployeeJDBCRepository");
        this.dbUtils = dbUtils;
        employeeValidator = new EmployeeValidator();
    }

    @Override
    public int size() {
        LOGGER.traceEntry();
        try {
            Connection connection = dbUtils.getConnection();
            String selectQuery = "select count(*) as SIZE from employees";
            return RepositoryUtils.getTableSize(connection, selectQuery);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Employee entity) throws ValidationException {
        LOGGER.traceEntry("Save employee {}", entity);
        employeeValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String insertQuery = "insert into employees values(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, entity.getEmployeeID());
                preparedStatement.setString(2, entity.getFirstName());
                preparedStatement.setString(3, entity.getLastName());
                preparedStatement.setString(4, entity.getUsername());
                preparedStatement.setString(5, entity.getPassword());
                preparedStatement.setString(6, entity.getOffice());
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " row(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer integer, Employee entity) throws ValidationException {
        LOGGER.traceEntry("Update employee {}", entity);
        employeeValidator.validate(entity);
        try {
            Connection connection = dbUtils.getConnection();
            String updateQuery = "update employees set first_name=?, last_name=?, username=?, password=?, office=?" +
                    " where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, entity.getFirstName());
                preparedStatement.setString(2, entity.getLastName());
                preparedStatement.setString(3, entity.getUsername());
                preparedStatement.setString(4, entity.getPassword());
                preparedStatement.setString(5, entity.getOffice());
                preparedStatement.setInt(6, entity.getEmployeeID());
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
        LOGGER.traceEntry("Deleting employee with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            RepositoryUtils.deleteEntity(connection, "employees", integer);
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public Employee findOne(Integer integer) {
        LOGGER.traceEntry("Finding employee with id={}", integer);
        try {
            Connection connection = dbUtils.getConnection();
            String findByIDQuery = "select * from employees where id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByIDQuery)) {
                preparedStatement.setInt(1, integer);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String office = resultSet.getString("office");
                        Employee employee = new Employee(id, firstName, lastName, username, password, office);
                        LOGGER.traceExit();
                        return employee;
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
    public List<Employee> findAll() {
        LOGGER.traceEntry();
        List<Employee> employeeList = new ArrayList<>();
        try {
            Connection connection = dbUtils.getConnection();
            String findAllQuery = "select * from employees";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String office = resultSet.getString("office");
                        Employee employee = new Employee(id, firstName, lastName, username, password, office);
                        employeeList.add(employee);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        LOGGER.traceExit(employeeList);
        return employeeList;
    }

    public boolean login(String username, String pass) {
        LOGGER.traceEntry();
        try {
            Connection connection = dbUtils.getConnection();
            String findByUsernameQuery = "SELECT * from employees where username=? and password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(findByUsernameQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, pass);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return false;
    }
}
