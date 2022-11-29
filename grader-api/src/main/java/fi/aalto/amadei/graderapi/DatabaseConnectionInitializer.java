package fi.aalto.amadei.graderapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseConnectionInitializer {
    private final Logger logger = LoggerFactory.getLogger(DatabaseConnectionInitializer.class);

    @Value("${DB_USER:postgres}")
    private String dbUser;

    @Value("${DB_PASSWORD:Bergamo99}")
    private String dbPassword;

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_DATABASE:grader}")
    private String dbName;

    private static Connection connection;

    @PostConstruct
    public void init() throws UnavailableException {
        String dbURL = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
        String dbDriver = "org.postgresql.Driver";

        try {
            Class.forName(dbDriver);

            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Couldn't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't connect to database");
        }

        if(connection == null)
            throw new UnavailableException("Couldn't connect to database");

        logger.info("Successfully connected to database");
    }

    public static Connection getConnection() {
        return connection;
    }
}
