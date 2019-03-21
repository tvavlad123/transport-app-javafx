package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RepositoryUtils {

    public static final Logger LOGGER = LogManager.getLogger();

    public static int getTableSize(Connection connection, String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LOGGER.traceExit(resultSet.getInt("SIZE"));
                    return resultSet.getInt("SIZE");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    public static void deleteEntity(Connection connection, String entity, int id) {
        LOGGER.traceEntry("Deleting entity with id={}", id);
        try {
            String deleteQuery = String.format("delete from %s where id=?", entity);
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                int result = preparedStatement.executeUpdate();
                LOGGER.traceExit(result + " row(s) affected");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }
    }
}
