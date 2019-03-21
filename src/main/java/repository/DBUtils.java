package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DBUtils {

    private String property;

    private static final Logger LOGGER = LogManager.getLogger();

    public DBUtils(String property) {
        this.property = property;
    }

    /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException when the connection fails
     */
    public Connection getConnection() throws SQLException {
        LOGGER.traceEntry();
        Connection conn = null;
        URL resource = DBUtils.class.getClassLoader().getResource(property);
        File file;
        try {
            file = new File(Objects.requireNonNull(resource).toURI());
            FileInputStream input = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(input);

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            conn = DriverManager.getConnection(url, user, password);
            LOGGER.info("Loaded JDBC driver");
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }

        return conn;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
